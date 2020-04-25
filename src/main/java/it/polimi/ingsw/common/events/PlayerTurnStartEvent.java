package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Player;

/**
 * Event for the turn start
 */
public class PlayerTurnStartEvent extends AbstractPlayerEvent {

    public PlayerTurnStartEvent(Player player) {
        super(player);
    }

}
