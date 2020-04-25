package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Player;

/**
 * Event for a player victory
 */

public class PlayerWinListener extends AbstractPlayerEvent {

    public PlayerWinListener(Player player) {
        super(player);
    }

}
