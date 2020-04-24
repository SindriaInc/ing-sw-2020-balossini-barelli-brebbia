package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Player;

public class PlayerWinListener extends AbstractPlayerEvent {

    public PlayerWinListener(Player player) {
        super(player);
    }

}
