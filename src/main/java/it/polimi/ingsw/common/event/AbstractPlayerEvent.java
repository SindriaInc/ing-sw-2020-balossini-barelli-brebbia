package it.polimi.ingsw.common.event;

/**
 * Abstract class for events which concern the player
 */
public abstract class AbstractPlayerEvent extends AbstractEvent {

    private final String player;

    public AbstractPlayerEvent(String player) {
        this.player = player;
    }

    @Override
    public boolean isValid() {
        if (getSender().isEmpty()) {
            return true;
        }

        return getSender().get().equals(player);
    }

    public String getPlayer() {
        return player;
    }

}
