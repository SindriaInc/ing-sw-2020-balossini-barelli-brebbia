package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.event.AbstractPlayerEvent;

import java.util.Optional;

public class AbstractRequestEvent extends AbstractPlayerEvent {

    public AbstractRequestEvent(String player) {
        super(player);
    }

    @Override
    public Optional<String> getSender() {
        return Optional.empty();
    }

}
