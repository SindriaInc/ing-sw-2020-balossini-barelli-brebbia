package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Player;

public class PlayerChooseGodEvent extends AbstractPlayerEvent {

    public PlayerChooseGodEvent(Player player) {
        super(player);
    }

}
