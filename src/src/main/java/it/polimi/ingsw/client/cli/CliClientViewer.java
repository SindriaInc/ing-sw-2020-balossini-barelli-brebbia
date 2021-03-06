package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.AbstractClientViewer;
import it.polimi.ingsw.client.cli.view.*;
import it.polimi.ingsw.client.clientstates.*;
import it.polimi.ingsw.common.io.InboundHandler;
import it.polimi.ingsw.common.io.OutboundHandler;
import it.polimi.ingsw.common.io.PrintableOutboundHandler;
import it.polimi.ingsw.common.logging.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static it.polimi.ingsw.client.cli.CliConstants.CLEAR;
import static it.polimi.ingsw.client.cli.CliConstants.TERM_WIDTH;

/**
 * The class settle the inbound and outbound handler for the cli.
 * Also load all asset files into string arrays
 */
public class CliClientViewer extends AbstractClientViewer {

    private static final String SPECIAL_COMMAND_PREFIX = "!";

    private static final String HEADER_FILE = "header-santorini.txt";

    private static final String SIDE_FILE = "side-bolt.txt";

    private static final String WIN_FILE = "win.txt";

    private static final String LOSE_FILE = "lose.txt";

    private static final String INVALID_COMMAND = "Invalid command, please type one of the available commands: ";

    /**
     * The service executor
     */
    private final ExecutorService executorService;

    /**
     * The outbound handler
     */
    private final OutboundHandler cliOutboundHandler;

    /**
     * The commands available to the user
     * The key of the map is the command label, the value is the command executor
     */
    private final List<CliCommand> commands = new ArrayList<>();

    /**
     * The header used as the application logo
     */
    private final String[] header;

    /**
     * The side banner used as a filler
     */
    private final String[] side;

    /**
     * The win banner
     */
    private final String[] win;

    /**
     * The lose banner
     */
    private final String[] lose;

    /**
     * The last printed view
     */
    private String lastOutput = "";

    /**
     * Class constructor, set the executor service, inbound handler and outbound handler
     * Load all asset files into string arrays
     * Call a consumer when the setup ends for launching client connector
     *
     * @param executorService The executor service
     * @param postStartup A consumer that will be called after the cli setup has ended
     */
    public CliClientViewer(ExecutorService executorService, Consumer<AbstractClientViewer> postStartup) {
        this.executorService = executorService;

        InboundHandler inboundHandler = new InboundHandler(System.in, this::onInput);
        cliOutboundHandler = new PrintableOutboundHandler(System.out, (ignored) -> {});

        executorService.submit(inboundHandler);
        executorService.submit(cliOutboundHandler);

        String[] header;
        String[] side;
        String[] win;
        String[] lose;
        try {
            header = loadLinesAsset(HEADER_FILE);
            side = loadLinesAsset(SIDE_FILE);
            win = loadLinesAsset(WIN_FILE);
            lose = loadLinesAsset(LOSE_FILE);
        } catch (IOException exception) {
            header = null;
            side = null;
            win = null;
            lose = null;
            Logger.getInstance().exception(exception);
            Logger.getInstance().severe("Unable to initialize the client, shutting down");
        }

        this.header = header;
        this.side = side;
        this.win = win;
        this.lose = lose;

        postStartup.accept(this);
    }

    /**
     * Check and execute an input message
     * @param message The message
     */
    private void onInput(String message) {
        String[] split = message.split(" ");
        String label = split[0];

        Optional<CliCommand> nonLabelHandler = getNonLabelHandler();

        if (nonLabelHandler.isPresent()) {
            onCommand(nonLabelHandler.get(), split);
            return;
        }

        // Handle normal commands

        Optional<CliCommand> optionalCommand = commands.stream()
                .filter(command -> command.getLabel().equals(label))
                .findFirst();

        if (optionalCommand.isEmpty()) {
            String output = CLEAR + lastOutput + viewCommands(INVALID_COMMAND);
            cliOutboundHandler.scheduleMessage(output);
            return;
        }

        onCommand(optionalCommand.get(), Arrays.copyOfRange(split, 1, split.length));
    }

    /**
     * Execute a command
     * @param command The command
     * @param arguments Its arguments
     */
    private void onCommand(CliCommand command, String[] arguments) {
        Optional<String> result = command.execute(arguments);

        if (result.isEmpty()) {
            return;
        }

        String output = CLEAR + lastOutput + "[!] " + result.get();
        cliOutboundHandler.scheduleMessage(output);
    }

    /**
     * Get handler for strings without a command
     * @return The handler
     */
    private Optional<CliCommand> getNonLabelHandler() {
        return commands.stream()
                .filter(command -> command.getLabel() == null)
                .findFirst();
    }

    /**
     * @see AbstractClientViewer#viewInput(InputState)
     */
    @Override
    public void viewInput(InputState state) {
        AbstractCliView abstractView = new CliInputView(state, header, TERM_WIDTH);
        updateView(state, abstractView);
    }

    /**
     * @see AbstractClientViewer#viewLogin(LoginState)
     */
    @Override
    public void viewLogin(LoginState state) {
        AbstractCliView abstractView = new CliLoginView(state, header, TERM_WIDTH);
        updateView(state, abstractView);
    }

    /**
     * @see AbstractClientViewer#viewLobby(LobbyState)
     */
    @Override
    public void viewLobby(LobbyState state) {
        AbstractCliView abstractView = new CliLobbyView(state, side, TERM_WIDTH);
        updateView(state, abstractView);
    }

    /**
     * @see AbstractClientViewer#viewRoom(RoomState)
     */
    @Override
    public void viewRoom(RoomState state) {
        AbstractCliView abstractView = new CliRoomView(state, side, TERM_WIDTH);
        updateView(state, abstractView);
    }

    /**
     * @see AbstractClientViewer#viewGame(GameState)
     */
    @Override
    public void viewGame(GameState state) {
        AbstractCliView abstractView = new CliGameView(state, TERM_WIDTH);
        updateView(state, abstractView);
    }

    /**
     * @see AbstractClientViewer#viewEnd(EndState)
     */
    @Override
    public void viewEnd(EndState state) {
        AbstractCliView abstractView = new CliEndView(state, win, lose, TERM_WIDTH);
        updateView(state, abstractView);
    }

    /**
     * @see AbstractClientViewer#shutdown()
     */
    @Override
    public void shutdown() {
        executorService.shutdownNow();
    }

    /**
     * Update the view
     * @param clientState The client state
     * @param abstractView The view to be generated
     */
    private void updateView(AbstractClientState clientState, AbstractCliView abstractView) {
        updateCommands(clientState, abstractView.generateCommands());
        String view = abstractView.generateView();

        String output = CLEAR + view + viewCommands("Type one of the commands to continue: ");
        cliOutboundHandler.scheduleMessage(output);

        lastOutput = view;
    }

    /**
     * Update the usable commands
     * @param clientState The client state
     * @param generatedCommands The list of commands
     */
    private void updateCommands(AbstractClientState clientState, List<CliCommand> generatedCommands) {
        commands.clear();
        commands.add(new CliCommand(SPECIAL_COMMAND_PREFIX + "stop", "Close the application", (parameters) -> {
            clientState.shutdown();
            executorService.shutdownNow();
            return Optional.empty();
        }));
        commands.addAll(generatedCommands);
    }

    /**
     * Get a string with commands
     * @param message The message
     * @return The string
     */
    private String viewCommands(String message) {
        if (getNonLabelHandler().isPresent()) {
            return "";
        }

        if (commands.stream().filter(command -> !command.getLabel().startsWith(SPECIAL_COMMAND_PREFIX)).count() <= 0) {
            return "";
        }

        StringBuilder output = new StringBuilder();
        output.append(message != null ? message : "");
        for (CliCommand command : commands) {
            if (command.getLabel().startsWith(SPECIAL_COMMAND_PREFIX)) {
                continue;
            }

            output.append(System.lineSeparator());

            StringBuilder arguments = new StringBuilder();
            Arrays.stream(command.getArguments()).forEach(argument -> arguments.append(" ").append(argument));

            output.append(command.getLabel()).append(arguments.toString()).append(" - ").append(command.getDescription());
        }

        return output.toString();
    }

    /**
     * Load a textual asset and turn it in a string array
     * @param file The file's path
     * @return The asset as a string array
     * @throws IOException Exception thrown when file isn't a valid path
     */
    private String[] loadLinesAsset(String file) throws IOException {
        file = ASSETS_DIRECTORY + file;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file);

        if (inputStream == null) {
            throw new IOException("Asset not found: " + file);
        }

        Reader streamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(streamReader);

        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        return lines.toArray(new String[0]);
    }

}
