package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.CliConstants;
import it.polimi.ingsw.client.clientstates.EndState;
import it.polimi.ingsw.client.clientstates.RoomState;
import it.polimi.ingsw.common.info.RoomInfo;

import java.util.List;

public class CliEndView extends AbstractCliView {

    private final EndState state;

    private final String[] win;

    private final String[] lose;

    public CliEndView(EndState state, String[] win, String[] lose, int lineLength) {
        super(lineLength);

        this.state = state;
        this.win = win;
        this.lose = lose;
    }

    @Override
    public String generateView() {
        String name = state.getData().getName();
        String winner = state.getData().getWinner();

        StringBuilder output = new StringBuilder();
        output.append(separator());
        output.append(System.lineSeparator().repeat(CliConstants.STATUS_SPACING));

        if (name.equals(winner)) {
            output.append(System.lineSeparator().repeat(CliConstants.HEADER_SPACING));
            output.append(buildHeader(win));
            output.append(System.lineSeparator().repeat(CliConstants.HEADER_SPACING));
        } else {
            output.append(System.lineSeparator().repeat(CliConstants.HEADER_SPACING));
            output.append(buildHeader(lose));

            if (state.getData().getWinner()==null) {winner="An opponent disconnected. Nobody";}
            output.append(System.lineSeparator().repeat(2)).append(center(winner + " has won the game"));
        }
        output.append(System.lineSeparator().repeat(CliConstants.HEADER_SPACING));
        output.append(System.lineSeparator().repeat(CliConstants.STATUS_SPACING));
        output.append(separator());

        return output.toString();
    }

    @Override
    public List<CliCommand> generateCommands() {
        return List.of();
    }

}
