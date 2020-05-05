package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class PreInitGame extends AbstractGameState {

    public PreInitGame(ModelEventProvider provider) {
        super(provider, null, List.of());
    }

    @Override
    public Player getCurrentPlayer() {
        return null;
    }

    @Override
    public AbstractGameState nextState() {
        return this;
    }

}
