package it.polimi.ingsw.common.event.request;

/**
 * Request a ping response from the player
 * The response will be used to determine whether or not the player is still connected
 */
public class RequestPlayerPingEvent extends AbstractRequestEvent {

    public RequestPlayerPingEvent(String player) {
        super(player);
    }

}
