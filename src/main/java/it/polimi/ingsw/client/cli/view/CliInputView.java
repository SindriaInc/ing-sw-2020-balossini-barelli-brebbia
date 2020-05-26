package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.CliConstants;
import it.polimi.ingsw.client.clientstates.InputState;

import java.util.List;
import java.util.Optional;

public class CliInputView extends AbstractCliView {

    private final InputState state;
    private final String[] header;

    public CliInputView(InputState state, String[] header, int lineLength) {
        super(lineLength);

        this.state = state;
        this.header = header;
    }

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

    @Override
    public List<CliCommand> generateCommands() {
        if (state.getData().getIp().isEmpty()) {
            return List.of(new CliCommand(this::onInsertIp));
        } else if (state.getData().getPort().isEmpty()) {
            return List.of(new CliCommand(this::onInsertPort));
        }

        return List.of();
    }

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