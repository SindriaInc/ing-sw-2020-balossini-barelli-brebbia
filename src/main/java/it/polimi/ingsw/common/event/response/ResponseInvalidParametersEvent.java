package it.polimi.ingsw.common.event.response;

import it.polimi.ingsw.controller.ResponseEventProvider;

/**
 * Event sent when the player sends an event with invalid parameters
 * This event should be used when a player send an expected event but using non-expected parameters
 */
public class ResponseInvalidParametersEvent extends AbstractResponseEvent {

    public ResponseInvalidParametersEvent(String player) {
        super(player);
    }

    @Override
    public void accept(ResponseEventProvider provider) {
        provider.getResponseInvalidParametersEventObservable().notifyObservers(this);
    }

}
