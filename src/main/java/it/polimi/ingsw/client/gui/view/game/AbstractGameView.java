package it.polimi.ingsw.client.gui.view.game;

import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.gui.view.AbstractGuiView;

public abstract class AbstractGameView extends AbstractGuiView {

    private GameState state;

    public AbstractGameView(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

}
