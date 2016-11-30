package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.inject.Inject;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.ResponseService;
import views.html.allResponses;
import views.html.response;
import views.html.thankPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This controller provides an access to users response actions
 * There are such fields:
 * COUNT is a number of {@link UserAnswer},
 * publisher is a reference to an {@code ActorRef} for a websocet purposes,
 * service provides access to the {@link ResponseService} methods,
 * fields reference which is used for {@link Publisher}
 */
public class ResponseController extends Controller {

    public static int COUNT;
    private ActorRef publisher;
    private FormFactory formFactory;
    private ResponseService service;
    private List<Field> fields;

    @Inject
    public ResponseController(FormFactory formFactory, ResponseService service, ActorSystem system) {
        this.formFactory = formFactory;
        this.service = service;
        publisher = system.actorOf(Props.create(Publisher.class), "publisher");
    }

    /**
     * A method where {@link UserAnswer} is collected.
     * Retrieve all {@link Field} from the DB and delete some if they are not active now.
     *
     * @return The response page would be returned.
     */
    @Transactional
    public Result index() {
        List<Field> allFields = JPA.em().createNamedQuery("getAll", Field.class).getResultList();
        if (allFields.isEmpty()) {
            return ok("Sorry, we do not working yet");
        }
        List<Field> fields = service.removeIsNotActive(allFields);
        return ok(response.render(fields));
    }

    /**
     * This method provides an access to an allResponses page with the websocket.
     * Retrieve a list of {@link Field} and a list of {@link UserAnswer}.
     * Then construct a list of {@link UsersAnswerDto} and pass it to the allResponses page.
     * <p>
     * Only the logged in admin has access for this page.
     *
     * @return The allResponses page.
     */
    @Transactional
    @Security.Authenticated(SecuredAdmin.class)
    public Result getAll() {
        fields = JPA.em().createNamedQuery("getAll", Field.class).getResultList();
        List<String> tableHeaders = new ArrayList<>(fields.size());

        //only labels are needed
        tableHeaders.addAll(
                fields.stream().
                        map(field -> field.label).
                        collect(Collectors.toList()));

        List<UserAnswer> users = JPA.em().
                createNamedQuery("getAllUsers", UserAnswer.class).
                getResultList();

        COUNT = users.size();

        List<UsersAnswerDto> answerDto = service.buildUserAnswerDto(fields, users);

        return ok(allResponses.render(tableHeaders, answerDto));
    }


    /**
     * Here we get a dynamic form from request, pass it to the {@link ResponseService}.
     * Construct a response to notify the allResponses page which uses the websocet.
     * We use the stored fields from the reference for that case if one admin
     * will delete some fields when another one is still on the allResponses page.
     *
     * @return The thankPage page would be returned.
     */
    @Transactional
    public Result getResponse() {
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();
        UserAnswer userAnswer = service.buildUserAnswer(dynamicForm);
        //save the user answer with his content relying on CascadeType.PERSIST
        JPA.em().persist(userAnswer);

        List<UserAnswer> userAnswers = new ArrayList<>();
        userAnswers.add(userAnswer);

        if (fields == null) {
            fields = JPA.em().createNamedQuery("getAll", Field.class).getResultList();
        }
        List<UsersAnswerDto> usersAnswerDto = service.buildUserAnswerDto(fields, userAnswers);

        StringBuilder sb = new StringBuilder();
        usersAnswerDto.get(0).
                answer.forEach(s -> sb.append("<td>").append(s).append("</td>"));

        publisher.tell(sb.toString(), null);
        return ok(thankPage.render());
    }
}
