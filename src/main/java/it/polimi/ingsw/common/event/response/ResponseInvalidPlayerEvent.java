package it.polimi.ingsw.common.event.response;

import it.polimi.ingsw.controller.ResponseEventProvider;

/**
 * Event sent when the player sends an event when no events (except for pings) where expected from the player
 */
public class ResponseInvalidPlayerEvent extends AbstractResponseEvent {

    /**
     * Class constructor
     *
     * @param player The player that will receive the event
     */
    public ResponseInvalidPlayerEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ResponseEventProvider provider) {
        provider.getResponseInvalidPlayerEventObservable().notifyObservers(this);
    }

}
