package it.polimi.ingsw.common.event;

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
