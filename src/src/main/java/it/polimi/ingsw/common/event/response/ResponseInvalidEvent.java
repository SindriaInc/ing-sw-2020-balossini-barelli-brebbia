package it.polimi.ingsw.common.event.response;

/**
 * Event sent when the player sends an invalid event (Bad client)
 * 
 * This event is handled by the VirtualView and should never be triggered by a valid client,
 * therefore it does not pass through the ResponseEventProvider
 */
public class ResponseInvalidEvent extends AbstractResponseEvent {

    /**
     * Class constructor
     *
     * @param player The player that will receive the event
     */
    public ResponseInvalidEvent(String player) {
        super(player);
    }

}
