package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Implement authorization.
 * getUserName() and onUnauthorized override superclass methods to restrict
 * access to all pages except the root '/' and '/login' to admin.
 */
public class SecuredAdmin extends Security.Authenticator {

    /**
     * Used by authentication annotation to determine if user is logged in.
     *
     * @param ctx The context.
     * @return The email address of the logged in user, or null if not logged in.
     */
    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get("admin");
    }

    /**
     * Instruct authenticator to automatically redirect to login page if unauthorized.
     *
     * @param ctx The context.
     * @return The login page.
     */
    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(routes.Login.login());
    }
}
