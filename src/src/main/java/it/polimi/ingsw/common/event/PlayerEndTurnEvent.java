package it.polimi.ingsw.common.event;

import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for the turn end
 *
 * View -> Model
 */
public class PlayerEndTurnEvent extends AbstractPlayerEvent  {

    /**
     * Class constructor
     *
     * @param player The name of the player that is ending the turn
     */
    public PlayerEndTurnEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerEndTurnEventObservable().notifyObservers(this);
    }

}
