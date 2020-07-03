package it.polimi.ingsw.common.event;

/**
 * Abstract class for events which concern the player
 */
public abstract class AbstractPlayerEvent extends AbstractEvent {

    /**
     * The player related to the event
     */
    private final String player;

    /**
     * Abstract class constructor
     *
     * @param player The player
     */
    public AbstractPlayerEvent(String player) {
        this.player = player;
    }

    /**
     * {@inheritDoc}
     */
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
