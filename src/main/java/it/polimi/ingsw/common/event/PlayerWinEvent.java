package it.polimi.ingsw.common.event;

import it.polimi.ingsw.model.ModelEventProvider;

/**
 * Event for a player victory, ends the game
 * PlayerWinEvent#getPlayer() may return null if there's no winner
 *
 * Model -> View
 */
public class PlayerWinEvent extends AbstractPlayerEvent {

    /**
     * Class constructor
     *
     * @param player The winner, or null if there's no winner
     */
    public PlayerWinEvent(String player) {
        super(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerWinEventObservable().notifyObservers(this);
    }

}
