package it.polimi.ingsw.common.event;

import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event sent by the player to notify that the connection is still working
 * Sent in response to a RequestPlayerPingEvent
 *
 * View -> Model
 */
public class PlayerPingEvent extends AbstractPlayerEvent {

    /**
     * Class constructor
     *
     * @param player The player that sent the ping
     */
    public PlayerPingEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerPingEventObservable().notifyObservers(this);
    }

}
