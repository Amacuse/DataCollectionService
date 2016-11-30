package models;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.contrib.pattern.DistributedPubSubExtension;
import akka.contrib.pattern.DistributedPubSubMediator;
import play.mvc.WebSocket;

/**
 * A generalized model of a Subscriber actor
 */
public class Subscriber extends UntypedActor {

    /**
     * The {@link WebSocket} instances for this Subscriber
     */
    WebSocket.In<String> in;
    WebSocket.Out<String> out;

    public Subscriber(WebSocket.In<String> in, WebSocket.Out<String> out) {
        this.in = in;
        this.out = out;

        ActorRef mediator = DistributedPubSubExtension.get(getContext().system()).mediator();

        // subscribe to the topic named "events"
        mediator.tell(new DistributedPubSubMediator.Subscribe("events", getSelf()), getSelf());
    }

    /**
     * Processes incoming messages that have been sent to this {@code Actor} from the {@link Publisher}
     *
     * @param msg A {@code SubscribeAck} or a {@code String} that will be sent to the client through out
     */
    @Override
    public void onReceive(Object msg) {
        if (msg instanceof String) {
            out.write((String) msg);
        } else {
            unhandled(msg);
        }
    }
}
