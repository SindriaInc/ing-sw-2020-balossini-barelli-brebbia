package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.AbstractClientViewer;
import it.polimi.ingsw.client.ClientConnector;
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

import static it.polimi.ingsw.client.cli.CliConstants.CLEAR;
import static it.polimi.ingsw.client.cli.CliConstants.TERM_WIDTH;

public class CliClientViewer extends AbstractClientViewer {

    private static final String SPECIAL_COMMAND_PREFIX = "!";

    private static final String HEADER_FILE = "header-santorini.txt";

    private static final String SIDE_FILE = "side-bolt.txt";

    private static final String WIN_FILE = "win.txt";

    private static final String LOSE_FILE = "lose.txt";

    private static final String INVALID_COMMAND = "Invalid command, please type one of the available commands: ";

    private final ExecutorService executorService;

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

    public CliClientViewer(ExecutorService executorService) {
        this.executorService = executorService;

        InboundHandler inboundHandler = new InboundHandler(System.in, this::onInput);
        cliOutboundHandler = new PrintableOutboundHandler(System.out, (ignored) -> {});

        executorService.submit(inboundHandler);
        executorService.submit(cliOutboundHandler);
        // TODO: Register log reader

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

        // Initialize the connector after everything has loaded
        new ClientConnector(this);
    }

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

    private void onCommand(CliCommand command, String[] arguments) {
        Optional<String> result = command.execute(arguments);

        if (result.isEmpty()) {
            return;
        }

        String output = CLEAR + lastOutput + "[!] " + result.get();
        cliOutboundHandler.scheduleMessage(output);
    }

    private Optional<CliCommand> getNonLabelHandler() {
        return commands.stream()
                .filter(command -> command.getLabel() == null)
                .findFirst();
    }

    @Override
    public void viewInput(InputState state) {
        AbstractCliView abstractView = new CliInputView(state, header, TERM_WIDTH);
        updateView(state, abstractView);
    }

    @Override
    public void viewLogin(LoginState state) {
        AbstractCliView abstractView = new CliLoginView(state, header, TERM_WIDTH);
        updateView(state, abstractView);
    }

    @Override
    public void viewLobby(LobbyState state) {
        AbstractCliView abstractView = new CliLobbyView(state, side, TERM_WIDTH);
        updateView(state, abstractView);
    }

    @Override
    public void viewRoom(RoomState state) {
        AbstractCliView abstractView = new CliRoomView(state, side, TERM_WIDTH);
        updateView(state, abstractView);
    }

    @Override
    public void viewGame(GameState state) {
        AbstractCliView abstractView = new CliGameView(state, TERM_WIDTH);
        updateView(state, abstractView);
    }

    @Override
    public void viewEnd(EndState state) {
        AbstractCliView abstractView = new CliEndView(state, win, lose, TERM_WIDTH);
        updateView(state, abstractView);
    }

    private void updateView(AbstractClientState clientState, AbstractCliView abstractView) {
        updateCommands(clientState, abstractView.generateCommands());
        String view = abstractView.generateView();

        String output = CLEAR + view + viewCommands("Type one of the commands to continue: ");
        cliOutboundHandler.scheduleMessage(output);

        lastOutput = view;
    }

    private void updateCommands(AbstractClientState clientState, List<CliCommand> generatedCommands) {
        commands.clear();
        commands.add(new CliCommand(SPECIAL_COMMAND_PREFIX + "stop", "Close the application", (parameters) -> {
            clientState.shutdown();
            executorService.shutdownNow();
            return Optional.empty();
        }));
        commands.addAll(generatedCommands);
    }

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
