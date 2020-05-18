package it.polimi.ingsw.common.event;

import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event sent by the player to notify that the connection is still working
 * Sent in response to a RequestPlayerPingEvent
 *
 * View -> Model
 */
public class PlayerPingEvent extends AbstractPlayerEvent {

    public PlayerPingEvent(String player) {
        super(player);
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerPingEventObservable().notifyObservers(this);
    }

}
