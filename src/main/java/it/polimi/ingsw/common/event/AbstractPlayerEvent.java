package it.polimi.ingsw.common.event;

import java.util.Optional;

/**
 * Abstract class for events which concern the player
 */
public abstract class AbstractPlayerEvent extends AbstractEvent {

    private final String player;

    public AbstractPlayerEvent(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public Optional<String> getSender() {
        return Optional.of(player);
    }

}
