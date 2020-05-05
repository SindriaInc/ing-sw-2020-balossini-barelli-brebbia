package it.polimi.ingsw.common.event.response;

import it.polimi.ingsw.common.event.AbstractPlayerEvent;

public abstract class AbstractResponseEvent extends AbstractPlayerEvent {

    public AbstractResponseEvent(String player) {
        super(player);
    }

}
