package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.view.game.AbstractGameView;
import it.polimi.ingsw.client.cli.view.game.CliBoardView;
import it.polimi.ingsw.client.cli.view.game.CliGodsView;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;

import java.util.List;

/**
 * Generate the game phase view
 */
public class CliGameView extends AbstractCliView {

    /**
     * The view to be used.
     * <code>CliGodsView</code> if the game is in gods choosing/selection state
     * <code>CliBoardView</code> if the game is in game state
     */
    private final AbstractGameView delegatedView;

    /**
     * Class constructor, set the state of the client and the line length
     *
     * @param state The client state
     * @param lineLength The line length
     */
    public CliGameView(GameState state, int lineLength) {
        super(lineLength);

        GameData data = state.getData();

        if (data.isInGodsPhase()) {
            delegatedView = new CliGodsView(state, lineLength);
            return;
        }

        delegatedView = new CliBoardView(state, lineLength);
    }

    /**
     * @see AbstractCliView#generateView()
     */
    @Override
    public String generateView() {
        return delegatedView.generateView();
    }

    /**
     * @see AbstractCliView#generateCommands()
     */
    @Override
    public List<CliCommand> generateCommands() {
        return delegatedView.generateCommands();
    }

}