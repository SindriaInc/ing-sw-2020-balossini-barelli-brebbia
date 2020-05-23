package it.polimi.ingsw.client.cli.view.game;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;

import java.util.List;
import java.util.Optional;

public class CliGodsView extends AbstractGameView {

    public CliGodsView(GameState state, int lineLength) {
        super(state, lineLength);
    }

    @Override
    public String generateView() {
        // TODO: Implement gods view generation

        return getState().toString();
    }

    @Override
    public List<CliCommand> generateCommands() {
        GameData data = getState().getData();

        if (data.getSelectGodsData().isPresent()) {
            return List.of(new CliCommand(this::onInsertSelectGods));
        } else if (data.getChooseGodData().isPresent()) {
            return List.of(new CliCommand(this::onInsertChooseGod));
        }

        return List.of();
    }

    private Optional<String> onInsertSelectGods(String[] arguments) {
        // TODO: Implement god selection

        return Optional.empty();
    }

    private Optional<String> onInsertChooseGod(String[] arguments) {
        // TODO: Implement choosing a god

        return Optional.empty();
    }

}