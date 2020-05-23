package it.polimi.ingsw.client.cli.view.game;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.clientstates.GameState;

import java.util.List;

public class CliBoardView extends AbstractGameView {

    public CliBoardView(GameState state, int lineLength) {
        super(state, lineLength);
    }

    @Override
    public String generateView() {
        // TODO: Implement board view generation

        return getState().toString();
    }

    @Override
    public List<CliCommand> generateCommands() {
        // TODO: Implement board commands

        return List.of();
    }

}
