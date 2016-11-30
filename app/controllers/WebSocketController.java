package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.inject.Inject;
import models.Subscriber;
import play.mvc.Controller;
import play.mvc.LegacyWebSocket;
import play.mvc.Result;
import play.mvc.WebSocket;

import java.util.UUID;

/**
 * This controller provides a websocket mechanism and register a subscribers
 */
public class WebSocketController extends Controller {

    ActorSystem system;

    @Inject
    public WebSocketController(ActorSystem system) {
        this.system = system;
    }

    /**
     * Register a websocket
     */
    public Result eventJs() {
        return ok(views.js.event.render());
    }

    /**
     * Register subscribers
     */
    public LegacyWebSocket<String> eventWs() {
        return new LegacyWebSocket<String>() {
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
                final ActorRef subscriber = system.actorOf(Props.create(Subscriber.class, in, out),
                        "subscriber-" + UUID.randomUUID());
            }
        };
    }
}
