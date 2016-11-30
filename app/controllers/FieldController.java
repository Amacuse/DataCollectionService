package controllers;

import models.Field;
import models.FieldDto;
import org.hibernate.Hibernate;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.fieldEdit;

import javax.inject.Inject;

/**
 * This controller can persist or delete a {@link Field}.
 * Only the logged in admin has access for this page.
 */
@Security.Authenticated(SecuredAdmin.class)
public class FieldController extends Controller {

    private FormFactory formFactory;

    @Inject
    public FieldController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * Retrieve the {@link Field} from the form for editing or updating.
     * If there are some mistakes have been made when the form were populated
     * the admin would be redirected to the fieldEdit page where he can correct the mistakes.
     * <p>
     * If the {@link Field} id is '-1' create a new {@link Field} and persist it.
     * If the {@link Field} id is not '-1' retrieve the {@link Field} from the DB
     * and merge it.
     * <p>
     * In both cases before persisting or merging populate the {@link Field} with
     * a {@link FieldDto} populateField method.
     *
     * @param id An id of the {@link Field} which would be persisted or updated.
     * @return Redirect to the fields page.
     */
    @Transactional
    public Result postField(Long id) {
        Form<FieldDto> fieldForm = formFactory.form(FieldDto.class).bindFromRequest();

        if (fieldForm.hasErrors()) {
            return badRequest(fieldEdit.render(fieldForm, id));
        }

        FieldDto fieldDto = fieldForm.get();

        //create a new field
        if (id == -1) {
            models.Field field = new models.Field();
            fieldDto.populateField(field);
            JPA.em().persist(field);
        } else {
            models.Field field = JPA.em().find(models.Field.class, id);
            //to avoid LazyInitializationException
            Hibernate.initialize(field.content);
            fieldDto.populateField(field);

            JPA.em().merge(field);
        }

        return redirect(routes.AdminController.fields());
    }

    /**
     * Retrieve the {@link Field} from the DB by id and delete it.
     *
     * @param id An id of the {@link Field} which would be deleted.
     * @return Redirect to the fields page.
     */
    @Transactional
    public Result deleteField(Long id) {
        models.Field field = JPA.em().find(models.Field.class, id);
        JPA.em().remove(field);
        return redirect(routes.AdminController.fields());
    }
}
