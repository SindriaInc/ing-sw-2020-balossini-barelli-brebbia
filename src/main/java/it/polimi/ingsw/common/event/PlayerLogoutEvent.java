package it.polimi.ingsw.common.event;

/**
 * Event sent by the view to notify a player disconnection
 * This event may be sent by the client or sent directly from the server implementation
 *
 * View -> Model
 */
public class PlayerLogoutEvent extends AbstractPlayerEvent {

    public PlayerLogoutEvent(String player) {
        super(player);
    }

}
