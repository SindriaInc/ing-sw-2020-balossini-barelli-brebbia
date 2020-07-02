package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.CliConstants;
import it.polimi.ingsw.client.clientstates.EndState;
import it.polimi.ingsw.client.clientstates.RoomState;
import it.polimi.ingsw.common.info.RoomInfo;

import java.util.List;

/**
 * Generate the end cli view
 */
public class CliEndView extends AbstractCliView {

    /**
     * The client state
     */
    private final EndState state;

    /**
     * The win header
     */
    private final String[] win;

    /**
     * The lose header
     */
    private final String[] lose;

    /**
     * Class constructor, set the state, win header, lose header and line length to be used in the rendering
     *
     * @param state The client state
     * @param win The win header
     * @param lose The lose header
     * @param lineLength The line length
     */
    public CliEndView(EndState state, String[] win, String[] lose, int lineLength) {
        super(lineLength);

        this.state = state;
        this.win = win;
        this.lose = lose;
    }

    /**
     * @see AbstractCliView#generateView()
     */
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

    /**
     * @see AbstractCliView#generateCommands()
     */
    @Override
    public List<CliCommand> generateCommands() {
        return List.of();
    }

}
