package it.polimi.ingsw.client.cli;

import java.util.Optional;

/**
 * The representation of a command given by user
 */
public class CliCommand {

    /**
     * A handler for a command
     */
    public interface CliCommandExecutor {

        /**
         * Called when the cli reads a command, the string returned will be shown to the user if present
         * @param parameters The parameters
         * @return The textual response
         */
        Optional<String> execute(String[] parameters);

    }

    /**
     * The command label
     */
    private final String label;

    /**
     * The array of command arguments
     */
    private final String[] arguments;

    /**
     * The command description
     */
    private final String description;

    /**
     * The executor
     */
    private final CliCommandExecutor executor;

    /**
     * A command with no label and description, it will accept any input (not beginning with !) and won't allow any other command
     * @param executor The executor
     */
    public CliCommand(CliCommandExecutor executor) {
        this(null, null, executor);
    }

    /**
     * A normal command with no arguments, there can be multiple commands at the same time
     * @param label The label
     * @param description The description, used to print information on how to use the command
     * @param executor The executor
     */
    public CliCommand(String label, String description, CliCommandExecutor executor) {
        this(label, new String[0], description, executor);
    }

    /**
     * A normal command with determined arguments, there can be multiple commands at the same time
     * @param label The label
     * @param arguments The arguments, used to print information on what parameters to pass to the command
     * @param description The description, used to print information on how to use the command
     * @param executor The executor
     */
    public CliCommand(String label, String[] arguments, String description, CliCommandExecutor executor) {
        this.label = label;
        this.arguments = arguments;
        this.description = description;
        this.executor = executor;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String getDescription() {
        return description;
    }

    public Optional<String> execute(String[] parameters) {
        return executor.execute(parameters);
    }

}
