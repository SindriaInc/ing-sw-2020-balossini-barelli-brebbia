package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.view.game.AbstractGameView;
import it.polimi.ingsw.client.cli.view.game.CliBoardView;
import it.polimi.ingsw.client.cli.view.game.CliGodsView;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;

import java.util.List;

public class CliGameView extends AbstractCliView {

    private final AbstractGameView delegatedView;

    public CliGameView(GameState state, int lineLength) {
        super(lineLength);

        GameData data = state.getData();

        if (data.isInGodsPhase()) {
            delegatedView = new CliGodsView(state, lineLength);
            return;
        }

        delegatedView = new CliBoardView(state, lineLength);
    }

    @Override
    public String generateView() {
        return delegatedView.generateView();
    }

    @Override
    public List<CliCommand> generateCommands() {
        return delegatedView.generateCommands();
    }

}
