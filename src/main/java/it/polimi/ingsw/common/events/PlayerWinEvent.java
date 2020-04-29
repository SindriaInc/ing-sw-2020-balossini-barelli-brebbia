package it.polimi.ingsw.common.events;

/**
 * Event for a player victory
 *
 * Model -> View
 */
public class PlayerWinEvent extends AbstractPlayerEvent {

    public PlayerWinEvent(String player) {
        super(player);
    }

}
