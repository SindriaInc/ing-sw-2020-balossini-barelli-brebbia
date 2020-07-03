package it.polimi.ingsw.common.event.response;

import it.polimi.ingsw.controller.ResponseEventProvider;

/**
 * Event sent when the player sends an event that wasn't expected
 * This event should be used when a player send a non-expected event for the state the player is in
 */
public class ResponseInvalidStateEvent extends AbstractResponseEvent {

    /**
     * Class constructor
     *
     * @param player The player that will receive the event
     */
    public ResponseInvalidStateEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ResponseEventProvider provider) {
        provider.getResponseInvalidStateEventObservable().notifyObservers(this);
    }

}
