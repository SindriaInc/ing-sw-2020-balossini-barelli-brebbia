package it.polimi.ingsw.common.event;

import it.polimi.ingsw.model.ModelEventProvider;

/**
 * Event for a player victory, ends the game
 * PlayerWinEvent#getPlayer() may return null if there's no winner
 *
 * Model -> View
 */
public class PlayerWinEvent extends AbstractPlayerEvent {

    public PlayerWinEvent(String player) {
        super(player);
    }

    public PlayerWinEvent() {
        super(null);
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getPlayerWinEventObservable().notifyObservers(this);
    }

}
