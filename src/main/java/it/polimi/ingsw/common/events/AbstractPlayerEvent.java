package it.polimi.ingsw.common.events;

/**
 * Abstract class for events which concern the player
 */
public abstract class AbstractPlayerEvent {

    private final String player;

    public AbstractPlayerEvent(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

}
