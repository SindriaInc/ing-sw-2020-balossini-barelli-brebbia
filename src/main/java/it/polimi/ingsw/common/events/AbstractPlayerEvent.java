package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Player;

/**
 * Abstract class for events which concern the player
 */
public abstract class AbstractPlayerEvent {

    private final Player player;

    public AbstractPlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
