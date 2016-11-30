package controllers;

import models.Admin;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import javax.inject.Inject;

/**
 * This controller provides an authentication process.
 */
public class Login extends Controller {

    private FormFactory formFactory;

    @Inject
    public Login(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * Provides the login page.
     *
     * @return The login page.
     */
    public Result login() {
        Form<Admin> userForm = formFactory.form(Admin.class);
        return ok(login.render(userForm));
    }

    /**
     * Verify admin.
     * <p>
     * If a user does not pass the authentication he will be redirected to the login page
     * with the particular message.
     *
     * @return The fields page .
     */
    @Transactional
    public Result loginCheck() {
        Form<models.Admin> loginForm = formFactory.form(models.Admin.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(views.html.login.render(loginForm));
        }

        session("admin", "admin");

        return redirect(routes.AdminController.fields());
    }

    /**
     * Provides the logout.
     *
     * @return The redirect to the login page.
     */
    @Security.Authenticated(SecuredAdmin.class)
    public Result logout() {
        session().clear();
        return redirect(routes.Login.login());
    }
}
