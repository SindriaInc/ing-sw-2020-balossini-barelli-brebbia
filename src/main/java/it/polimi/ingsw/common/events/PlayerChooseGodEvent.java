package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Player;

/**
 * Event for the god choice by the player
 */
public class PlayerChooseGodEvent extends AbstractPlayerEvent {

    public PlayerChooseGodEvent(Player player) {
        super(player);
    }

}
