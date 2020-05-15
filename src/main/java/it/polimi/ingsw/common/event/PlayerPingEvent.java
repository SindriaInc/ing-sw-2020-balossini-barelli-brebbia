package it.polimi.ingsw.common.event;

/**
 * Event sent by the player to notify that the connection is still working
 * Sent in response to a RequestPlayerPingEvent
 *
 * View -> Model
 */
public class PlayerPingEvent extends AbstractPlayerEvent {

    public PlayerPingEvent(String player) {
        super(player);
    }

}
