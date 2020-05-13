package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.event.AbstractPlayerEvent;

import java.util.Optional;

public abstract class AbstractLobbyEvent extends AbstractPlayerEvent {

    public AbstractLobbyEvent(String player) {
        super(player);
    }

    @Override
    public Optional<String> getSender() {
        return Optional.empty();
    }

}
