package it.polimi.ingsw.common.event.response;

import it.polimi.ingsw.common.event.AbstractPlayerEvent;

import java.util.Optional;

public abstract class AbstractResponseEvent extends AbstractPlayerEvent {

    public AbstractResponseEvent(String player) {
        super(player);
    }

    @Override
    public Optional<String> getSender() {
        return Optional.empty();
    }

}
