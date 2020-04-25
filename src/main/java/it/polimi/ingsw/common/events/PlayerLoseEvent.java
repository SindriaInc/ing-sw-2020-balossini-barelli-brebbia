package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Player;

/**
 * Event for a player defeat
 */
public class PlayerLoseEvent extends AbstractPlayerEvent {

    public PlayerLoseEvent(Player player) {
        super(player);
    }

}
