package it.polimi.ingsw.common.event;

import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event sent by the view to notify a player disconnection
 * This event may be sent by the client or sent directly from the server implementation
 *
 * View -> Model
 */
public class PlayerLogoutEvent extends AbstractPlayerEvent {

    /**
     * Class constructor
     *
     * @param player The player that has disconnected
     */
    public PlayerLogoutEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerLogoutEventObservable().notifyObservers(this);
    }

}
