package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.model.ModelEventProvider;

/**
 * Request a PlayerPingEvent from the player
 * The response will be used to determine whether or not the player is still connected
 * This event is not related to other request events and can always be replied to
 *
 * This event is handled by the VirtualView and therefore does not pass through the ModelEventProvider
 */
public class RequestPlayerPingEvent extends AbstractRequestEvent {

    /**
     * Class constructor
     *
     * @param player The player that the request is targeted to
     */
    public RequestPlayerPingEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestPlayerPingEventObservable().notifyObservers(this);
    }

}
