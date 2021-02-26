package it.polimi.ingsw.common.event;

import it.polimi.ingsw.model.ModelEventProvider;

/**
 * Event for the turn start
 *
 * Model -> View
 */
public class PlayerTurnStartEvent extends AbstractPlayerEvent {

    /**
     * Class constructor
     *
     * @param player The player whose turn just started
     */
    public PlayerTurnStartEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerTurnStartEventObservable().notifyObservers(this);
    }

}
