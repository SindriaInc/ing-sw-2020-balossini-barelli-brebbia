package it.polimi.ingsw.common.event;

/**
 * Event for a player defeat
 *
 * Model -> View
 */
public class PlayerLoseEvent extends AbstractPlayerEvent {

    public PlayerLoseEvent(String player) {
        super(player);
    }

}
