package it.polimi.ingsw.common.event;

import it.polimi.ingsw.model.ModelEventProvider;

/**
 * Event for a player defeat
 *
 * Model -> View
 */
public class PlayerLoseEvent extends AbstractPlayerEvent {

    /**
     * Class constructor
     *
     * @param player The player that has lost
     */
    public PlayerLoseEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerLoseEventObservable().notifyObservers(this);
    }

}
