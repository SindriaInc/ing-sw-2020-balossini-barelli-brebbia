package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.CliConstants;
import it.polimi.ingsw.client.clientstates.LoginState;

import java.util.List;
import java.util.Optional;

public class CliLoginView extends AbstractCliView {

    private final LoginState state;
    private final String[] header;

    public CliLoginView(LoginState state, String[] header, int lineLength) {
        super(lineLength);

        this.state = state;
        this.header = header;
    }

    @Override
    public String generateView() {
        Optional<String> name = state.getData().getName();
        Optional<Integer> age = state.getData().getAge();
        Optional<String> lastMessage = state.getData().getLastMessage();

        StringBuilder output = new StringBuilder();

        output.append(separator()).append(System.lineSeparator().repeat(CliConstants.HEADER_SPACING));
        output.append(buildHeader(header));
        output.append(System.lineSeparator().repeat(CliConstants.HEADER_SPACING - 1));
        output.append(center("{ Now connected to the server }")).append(System.lineSeparator());

        name.ifPresent(string -> output.append(center("Your name: " + string)));
        output.append(System.lineSeparator());
        age.ifPresent(integer -> output.append(center("Your age: " + integer.toString())));

        output.append(System.lineSeparator().repeat(CliConstants.STATUS_SPACING));
        if (name.isPresent() && age.isPresent()) {
            output.append(center("Just wait a bit more. Login in progress..."));
        } else {
            lastMessage.ifPresent(string -> output.append(center(string)));
        }

        output.append(System.lineSeparator().repeat(CliConstants.STATUS_SPACING));

        output.append(separator());
        if (name.isEmpty()) {
            output.append("Insert your name: ");
        } else if (age.isEmpty()) {
            output.append("Insert your age: ");
        }

        return output.toString();
    }

    @Override
    public List<CliCommand> generateCommands() {
        if (state.getData().getName().isEmpty()) {
            return List.of(new CliCommand(this::onInsertName));
        } else if (state.getData().getAge().isEmpty()) {
            return List.of(new CliCommand(this::onInsertAge));
        }

        return List.of();
    }

    /**
     * Respond to an insert name command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onInsertName(String[] arguments) {
        if (arguments.length != 1) {
            return Optional.of("Spaces are not allowed in the name, please type a name without them");
        }

        String name = arguments[0];

        if (name.length() <= 0) {
            return Optional.of("Please insert your name");
        }

        state.acceptName(arguments[0]);
        return Optional.empty();
    }

    /**
     * Respond to an insert age command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onInsertAge(String[] arguments) {
        if (arguments.length != 1) {
            return Optional.of("Please type only your age");
        }

        try {
            state.acceptAge(Integer.parseInt(arguments[0]));
            state.acceptLogin();
            return Optional.empty();
        } catch (NumberFormatException exception) {
            return Optional.of("Your age must be a number");
        }
    }

}
