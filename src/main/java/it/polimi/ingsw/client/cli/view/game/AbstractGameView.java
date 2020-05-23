package it.polimi.ingsw.client.cli.view.game;

import it.polimi.ingsw.client.cli.view.AbstractCliView;
import it.polimi.ingsw.client.clientstates.GameState;

public abstract class AbstractGameView extends AbstractCliView {

    private final GameState state;

    public AbstractGameView(GameState state, int lineLength) {
        super(lineLength);

        this.state = state;
    }

    public GameState getState() {
        return state;
    }

}
