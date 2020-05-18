package it.polimi.ingsw.common.event;

import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for the turn end
 *
 * View -> Model
 */
public class PlayerEndTurnEvent extends AbstractPlayerEvent  {

    public PlayerEndTurnEvent(String player) {
        super(player);
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerEndTurnEventObservable().notifyObservers(this);
    }

}
