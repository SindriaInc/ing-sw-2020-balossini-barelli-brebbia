package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Player;

public abstract class AbstractPlayerEvent {

    private final Player player;

    public AbstractPlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
