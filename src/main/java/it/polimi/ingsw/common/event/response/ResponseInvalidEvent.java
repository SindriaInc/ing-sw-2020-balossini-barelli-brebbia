package it.polimi.ingsw.common.event.response;

/**
 * Event sent when the player sends an invalid event
 * 
 * This event is handled by the VirtualView and therefore does not pass through the ResponseEventProvider
 */
public class ResponseInvalidEvent extends AbstractResponseEvent {

    public ResponseInvalidEvent(String player) {
        super(player);
    }

}
