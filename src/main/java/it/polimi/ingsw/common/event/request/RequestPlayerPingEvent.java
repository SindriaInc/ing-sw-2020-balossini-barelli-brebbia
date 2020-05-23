package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.model.ModelEventProvider;

/**
 * Request a ping response from the player
 * The response will be used to determine whether or not the player is still connected
 * This event is not related to other request events and can always be replied to
 *
 * This event is handled by the VirtualView and therefore does not pass through the ModelEventProvider
 */
public class RequestPlayerPingEvent extends AbstractRequestEvent {

    public RequestPlayerPingEvent(String player) {
        super(player);
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestPlayerPingEventObservable().notifyObservers(this);
    }

}
