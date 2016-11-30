package controllers;

import play.routing.JavaScriptReverseRouter;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller provides an access to routes which can be used in javascript.
 */
public class Application extends Controller {

    public Result javascriptRoutes() {
        return ok(JavaScriptReverseRouter.create("jsRoutes",
                routes.javascript.FieldController.deleteField())).as("text/javascript");
    }
}
