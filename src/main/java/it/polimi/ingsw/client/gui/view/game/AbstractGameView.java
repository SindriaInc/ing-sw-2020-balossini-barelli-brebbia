package it.polimi.ingsw.client.gui.view.game;

import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.gui.GuiAssets;
import it.polimi.ingsw.client.gui.view.AbstractGuiView;

/**
 * A gui view of the game state
 */
public abstract class AbstractGameView extends AbstractGuiView {

    /**
     * The game data
     */
    private GameState state;

    /**
     * Abstract class constructor, set assets and the game state
     * @param state The game state
     * @param assets The gui assets
     */
    public AbstractGameView(GameState state, GuiAssets assets) {
        super(assets);

        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

}
