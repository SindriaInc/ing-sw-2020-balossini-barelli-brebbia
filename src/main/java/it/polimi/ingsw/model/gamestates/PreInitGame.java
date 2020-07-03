package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * The class representing the first state of game which precedes the start.
 */
public class PreInitGame extends AbstractGameState {

    /**
     * Class constructor
     * @param provider The provider of the events
     */
    public PreInitGame(ModelEventProvider provider) {
        super(provider, null, List.of());
    }

    /**
     * @see AbstractGameState#getCurrentPlayer()
     */
    @Override
    public Player getCurrentPlayer() {
        return null;
    }

    /**
     * @see AbstractGameState#nextState()
     */
    @Override
    public AbstractGameState nextState() {
        return this;
    }

}
