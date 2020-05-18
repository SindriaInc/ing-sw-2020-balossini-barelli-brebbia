package it.polimi.ingsw.common.event;

import it.polimi.ingsw.model.ModelEventProvider;

/**
 * Event for the turn start
 *
 * Model -> View
 */
public class PlayerTurnStartEvent extends AbstractPlayerEvent {

    public PlayerTurnStartEvent(String player) {
        super(player);
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerTurnStartEventObservable().notifyObservers(this);
    }

}
