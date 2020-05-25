package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.CliConstants;
import it.polimi.ingsw.client.clientstates.EndState;

import java.util.List;

public class CliEndView extends AbstractCliView {

    private final EndState state;

    public CliEndView(EndState state, int lineLength) {
        super(lineLength);

        this.state = state;
    }

    @Override
    public String generateView() {
        String name = state.getData().getName();
        String winner = state.getData().getWinner();

        StringBuilder output = new StringBuilder();
        output.append(separator());
        output.append(System.lineSeparator().repeat(CliConstants.STATUS_SPACING));
        if (name.equals(winner)) {
            output.append("Congratulations! You've won").append(System.lineSeparator());
        } else {
            output.append(winner).append(" has won the game").append(System.lineSeparator());
        }
        output.append(System.lineSeparator().repeat(CliConstants.STATUS_SPACING));
        output.append(separator());

        return output.toString();
    }

    @Override
    public List<CliCommand> generateCommands() {
        return List.of();
    }

}
