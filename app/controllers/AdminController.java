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
import views.html.*;

import javax.inject.Inject;
import java.util.List;

/**
 * This controller provides an access for the admin to
 * {@link Field} manipulation as get all fields, create a new one and edit an existed {@link Field}.
 */
public class AdminController extends Controller {

    private FormFactory formFactory;

    @Inject
    public AdminController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * Retrieve all persisted fields from DB.
     * <p>
     * If admin does not logged in he will be redirected to the login page.
     *
     * @return The fields page.
     */
    @Transactional
    public Result fields() {
        String admin = session("admin");
        if (admin == null) {
            return redirect(routes.Login.login());
        }
        List<Field> allFields = JPA.em().createNamedQuery("getAll", Field.class).getResultList();
        return ok(fields.render(allFields));
    }

    /**
     * Retrieve the {@link Field} from the DB for editing
     * Construct the {@link FieldDto} which will be passed to the fieldEdit page.
     * Fill the form with data from the {@link Field}.
     * <p>
     * Only the logged in admin has access for this page.
     *
     * @param id An id of the {@link Field} which would be edited.
     * @return The fieldEdit page.
     */
    @Security.Authenticated(SecuredAdmin.class)
    @Transactional
    public Result editField(Long id) {
        Field field = JPA.em().find(Field.class, id);

        //to avoid LazyInitializationException
        Hibernate.initialize(field.content);

        FieldDto fieldDto = new FieldDto(field);

        Form<FieldDto> fieldForm = formFactory.form(FieldDto.class).fill(fieldDto);
        return ok(fieldEdit.render(fieldForm, id));
    }

    /**
     * Construct the {@link FieldDto} which will be passed to the fieldEdit page.
     * Fill the form with data which preset up in {@link FieldDto}.
     * There is a dummy for the {@link Field} id that is needed to establish
     * whether this is a new {@link Field} or not.
     * <p>
     * Only the logged in admin has access for this page.
     *
     * @return The fieldEdit page.
     */
    @Security.Authenticated(SecuredAdmin.class)
    @Transactional
    public Result createField() {
        FieldDto fieldDto = new FieldDto();
        Form<FieldDto> fieldForm = formFactory.form(FieldDto.class).fill(fieldDto);

        //dummy for id
        Long id = -1L;
        return ok(fieldEdit.render(fieldForm, id));
    }
}
