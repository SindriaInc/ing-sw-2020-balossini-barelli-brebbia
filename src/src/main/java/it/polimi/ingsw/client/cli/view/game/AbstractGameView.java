package it.polimi.ingsw.client.cli.view.game;

import it.polimi.ingsw.client.cli.view.AbstractCliView;
import it.polimi.ingsw.client.clientstates.GameState;

/**
 * A view of a phase of the game
 */
public abstract class AbstractGameView extends AbstractCliView {

    /**
     * The game state
     */
    private final GameState state;

    /**
     * Class constructor, generate the view
     *
     * @param state The client state
     * @param lineLength The line length
     */
    public AbstractGameView(GameState state, int lineLength) {
        super(lineLength);

        this.state = state;
    }

    public GameState getState() {
        return state;
    }

}
