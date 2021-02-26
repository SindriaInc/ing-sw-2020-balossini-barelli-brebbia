package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.CliConstants;
import it.polimi.ingsw.client.clientstates.InputState;

import java.util.List;
import java.util.Optional;

/**
 * Generate the cli input view
 */
public class CliInputView extends AbstractCliView {

    /**
     * The client state
     */
    private final InputState state;

    /**
     * The header
     */
    private final String[] header;

    /**
     * Class constructor, set the client state, the header and the line length
     *
     * @param state The client state
     * @param header The header
     * @param lineLength The line length
     */
    public CliInputView(InputState state, String[] header, int lineLength) {
        super(lineLength);

        this.state = state;
        this.header = header;
    }

    /**
     * @see AbstractCliView#generateView()
     */
    @Override
    public String generateView() {
        Optional<String> ip = state.getData().getIp();
        Optional<Integer> port = state.getData().getPort();
        Optional<String> lastMessage = state.getData().getLastMessage();

        StringBuilder output = new StringBuilder();

        output.append(separator()).append(System.lineSeparator().repeat(CliConstants.HEADER_SPACING));
        output.append(buildHeader(header));
        output.append(System.lineSeparator().repeat(CliConstants.HEADER_SPACING));

        ip.ifPresent(string -> output.append(center("IP Address: " + string)));
        output.append(System.lineSeparator());
        port.ifPresent(integer -> output.append(center("Port: " + integer.toString())));

        output.append(System.lineSeparator().repeat(CliConstants.STATUS_SPACING));
        if (ip.isPresent() && port.isPresent()) {
            output.append(center("Connecting to server..."));
        } else {
            lastMessage.ifPresent(string -> output.append(center(string)));
        }

        output.append(System.lineSeparator().repeat(CliConstants.STATUS_SPACING));

        output.append(separator());
        if (ip.isEmpty()) {
            output.append("Insert the server ip address: ");
        } else if (port.isEmpty()) {
            output.append("Insert the server port address: ");
        }

        return output.toString();
    }

    /**
     * @see AbstractCliView#generateCommands()
     */
    @Override
    public List<CliCommand> generateCommands() {
        if (state.getData().getIp().isEmpty()) {
            return List.of(new CliCommand(this::onInsertIp));
        } else if (state.getData().getPort().isEmpty()) {
            return List.of(new CliCommand(this::onInsertPort));
        }

        return List.of();
    }

    /**
     * Respond to an insert IP command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onInsertIp(String[] arguments) {
        if (arguments.length != 1) {
            return Optional.of("Please type only the server ip");
        }

        String ip = arguments[0];

        if (ip.length() <= 0) {
            return Optional.of("Please insert the server ip");
        }

        state.acceptIp(arguments[0]);
        return Optional.empty();
    }

    /**
     * Respond to an insert port command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onInsertPort(String[] arguments) {
        if (arguments.length != 1) {
            return Optional.of("Please type only the server port");
        }

        try {
            state.acceptPort(Integer.parseInt(arguments[0]));
            state.acceptConnect();
            return Optional.empty();
        } catch (NumberFormatException exception) {
            return Optional.of("The server port must be a number");
        }
    }

}
