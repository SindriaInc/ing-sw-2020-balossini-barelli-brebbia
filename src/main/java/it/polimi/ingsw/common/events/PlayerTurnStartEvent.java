package it.polimi.ingsw.common.events;

/**
 * Event for the turn start
 *
 * Model -> View
 */
public class PlayerTurnStartEvent extends AbstractPlayerEvent {

    public PlayerTurnStartEvent(String player) {
        super(player);
    }

}
