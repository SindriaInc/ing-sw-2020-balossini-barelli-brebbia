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
     * Class constructor, set assets and the game state
     * @param state
     * @param images
     */
    public AbstractGameView(GameState state, GuiAssets images) {
        super(images);

        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

}
