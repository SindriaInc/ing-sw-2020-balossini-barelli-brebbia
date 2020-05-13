package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.IClient;
import it.polimi.ingsw.client.message.ErrorMessage;
import it.polimi.ingsw.client.socket.SocketClient;
import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.RoomInfo;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.lobby.LobbyGameStartEvent;
import it.polimi.ingsw.common.event.lobby.LobbyRoomUpdateEvent;
import it.polimi.ingsw.common.event.lobby.LobbyUpdateEvent;
import it.polimi.ingsw.common.event.request.*;
import it.polimi.ingsw.common.event.response.AbstractResponseEvent;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.serializer.GsonEventSerializer;
import it.polimi.ingsw.common.serializer.SerializationException;
import it.polimi.ingsw.model.Game;

import java.util.*;
import java.util.function.Predicate;

public class CliMain {

    private static class Command {

        private final String label;

        private final String argsSyntax;

        private final Predicate<String[]> executor;

        private Command(String label, String argsSyntax, Predicate<String[]> executor) {
            this.label = label;
            this.argsSyntax = argsSyntax;
            this.executor = executor;
        }

        public String getLabel() {
            return label;
        }

        public String getSyntax() {
            return label + (argsSyntax != null ? ":" + argsSyntax : "");
        }

        public Predicate<String[]> getExecutor() {
            return executor;
        }

    }

    private static class CellInfo {

        private final int x;
        private final int y;
        private int level;
        private boolean doomed;

        public CellInfo(int x, int y) {
            this.x = x;
            this.y = y;
            this.level = 0;
            this.doomed = false;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getLevel() {
            return level;
        }

        public boolean isDoomed() {
            return doomed;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public void setDoomed(boolean doomed) {
            this.doomed = doomed;
        }

    }

    private static class WorkerInfo {

        private final int id;
        private final String owner;
        private Coordinates position;

        public WorkerInfo(int id, String owner, Coordinates position) {
            this.id = id;
            this.owner = owner;
            this.position = position;
        }

        public int getId() {
            return id;
        }

        public String getOwner() {
            return owner;
        }

        public Coordinates getPosition() {
            return position;
        }

        public void setPosition(Coordinates position) {
            this.position = position;
        }

    }

    private enum CliState {

        INPUT,
        LOGIN,
        LOBBY,
        ROOM,
        GAME,
        SPECTATOR,
        END

    }

    private static final String COMMAND_STOP = "stop";

    private static final String COMMAND_JOIN = "join";

    private static final String COMMAND_CREATE = "create";

    private static final int SPACING = 7;

    private static final int TERM_HEIGHT = 20;

    private static final int TERM_WIDTH = 80;

    private static final String CLEAR = System.lineSeparator().repeat(TERM_HEIGHT);

    private static final String SEPARATOR_HIGH = "=".repeat(TERM_WIDTH);

    private static final String SEPARATOR_LOW = "-".repeat(TERM_WIDTH);

    /**
     * The input scanner
     */
    private final Scanner scanner;

    /**
     * The event serializer
     */
    private final GsonEventSerializer serializer;

    /**
     * The lines to be sent to the logger when flushing
     */
    private final List<String> output = new ArrayList<>();

    /**
     * The client instance, implementing the connection to the server
     */
    private IClient client;

    /**
     * Whether or not the client is waiting for an input by the user
     */
    private boolean waiting = true;

    /**
     * Whether or not the last event was accepted by the server
     */
    private boolean forwarded = false;

    /**
     * Whether or not the last event was rejected by the server
     */
    private boolean invalid = false;

    /**
     * Whether or not it is this player's turn
     */
    private boolean turn = false;

    /**
     * Whether or not the client has just sent a packet and it's waiting for a response
     */
    private boolean sent = false;

    /**
     * The current client state
     */
    private CliState state;

    /**
     * The list of rooms
     * Only available when in the lobby
     */
    private final List<RoomInfo> rooms = new ArrayList<>();

    /**
     * The current room
     * Only available when in a room
     */
    private RoomInfo room;

    /**
     * The player name, defined after the login event
     */
    private String player;

    /**
     * The winner name
     * Only available when the game has ended
     */
    private String winner;

    /**
     * The game map
     * Only available when in game
     */
    private final CellInfo[][] map = new CellInfo[Game.BOARD_COLUMNS][Game.BOARD_ROWS];

    /**
     * The workers
     * Only available when in game
     */
    private final HashMap<Integer, WorkerInfo> workers = new HashMap<>();

    /**
     * The list of commands that the user can type to progress in the game
     * Only available when in game
     */
    private final List<Command> availableCommands = new ArrayList<>();

    public CliMain() {
        scanner = new Scanner(System.in);
        serializer = new GsonEventSerializer();

        state = CliState.INPUT;
    }

    public void shutdown() {
        Logger logger = Logger.getInstance();
        logger.info("Shutting down, goodbye!");

        if (client != null) {
            client.shutdown();
        }

        logger.shutdown();
    }

    public void init() {
        boolean accepted = false;
        while (!accepted) {
            String ip = in("Server ip: ");
            int port = inInt("Server port: ");

            client = new SocketClient(ip, port);
            client.registerHandler(this::onMessage);
            client.registerHandler(this::onError);

            accepted = waitFor(CliState.LOGIN);
        }

        login();
    }

    private void login() {
        boolean accepted = false;
        while (!accepted) {
            player = in("Player name: ");
            int age = inInt("Age: ");

            client.send(serializer.serialize(new PlayerLoginEvent(player, age)));
            accepted = waitFor(CliState.LOBBY);
        }

        lobby();
    }

    private void lobby() {
        boolean accepted = false;
        while (!accepted) {
            String command = in("Insert command: ");
            String[] parameters = command.split(":");

            if (parameters.length <= 0) {
                out("Invalid command");
                continue;
            }

            String label = parameters[0];

            if (label.equals(COMMAND_CREATE)) {
                if (parameters.length != 3) {
                    out("Invalid parameters (Expected 3, got " + parameters.length + ")");
                    continue;
                }

                Integer maxPlayers = intOrNull(parameters[1]);

                if (maxPlayers == null) {
                    out("Invalid max players, must be a number");
                    continue;
                }

                boolean simple;

                if (parameters[2].equalsIgnoreCase("simple")) {
                    simple = true;
                } else if (parameters[2].equalsIgnoreCase("normal")) {
                    simple = false;
                } else {
                    out("Invalid game type, must be either simple or normal");
                    continue;
                }

                client.send(serializer.serialize(new PlayerCreateRoomEvent(player, maxPlayers, simple)));
            } else if (label.equals(COMMAND_JOIN)) {
                if (parameters.length != 2) {
                    out("Invalid parameters (Expected 2, got " + parameters.length + ")");
                    continue;
                }

                String owner = parameters[1];

                client.send(serializer.serialize(new PlayerJoinRoomEvent(player, owner)));
            } else {
                out("Invalid command");
            }

            forwarded = false;
            accepted = waitForResponse();
        }

        if (state == CliState.GAME) {
            // Skip to game state if the server says so
            game();
            return;
        }

        room();
    }

    private void room() {
        waitFor(CliState.GAME);

        game();
    }

    private void game() {
        while (state == CliState.GAME) {
            waitForTurn();

            boolean accepted = false;
            while (!accepted) {
                String command = in("Insert command: ");
                String[] parameters = command.split(":");

                if (parameters.length == 0) {
                    continue;
                }

                String label = parameters[0];
                String[] args = Arrays.copyOfRange(parameters, 1, parameters.length);

                for (Command availableCommand : availableCommands) {
                    if (!label.equals(availableCommand.getLabel())) {
                        continue;
                    }

                    sent = true;
                    forwarded = false;
                    if (availableCommand.getExecutor().test(args)) {
                        accepted = waitForResponse();
                        break;
                    }
                }
            }
        }

        end();
    }

    private void end() {
        client.shutdown();
        client = null;
        String command = in("Write \"restart\" to restart the client or anything else to close it: ");

        if (command.equals("restart")) {
            init();
            return;
        }

        shutdown();
    }

    private void waitForTurn() {
        while (!turn) {
            try {
                Thread.sleep(ClientMain.SLEEP_PERIOD_MS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    private boolean waitForResponse() {
        while (!forwarded && !invalid) {
            try {
                Thread.sleep(ClientMain.SLEEP_PERIOD_MS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        forwarded = false;

        if (invalid) {
            invalid = false;
            return false;
        }

        return true;
    }

    private boolean waitFor(CliState state) {
        while (this.state != state && !invalid) {
            try {
                Thread.sleep(ClientMain.SLEEP_PERIOD_MS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        if (invalid) {
            invalid = false;
            return false;
        }

        return true;
    }

    private void onMessage(String message) {
        if (message.equals(IClient.CONNECT_MESSAGE)) {
            state = CliState.LOGIN;
            out("Connection established");
            return;
        }

        AbstractEvent event;

        try {
            event = serializer.deserialize(message);
        } catch (SerializationException exception) {
            Logger.getInstance().exception(exception);
            return;
        }

        if (event instanceof AbstractResponseEvent) {
            invalid = true;
            return;
        }

        if (event instanceof LobbyUpdateEvent) {
            rooms.clear();
            rooms.addAll(((LobbyUpdateEvent) event).getRooms());

            printLobby();
            state = CliState.LOBBY;
        } else if (event instanceof LobbyRoomUpdateEvent) {
            room = ((LobbyRoomUpdateEvent) event).getRoomInfo();

            printRoom();
            state = CliState.ROOM;
        } else if (event instanceof LobbyGameStartEvent) {
            for (int x = 0; x < Game.BOARD_COLUMNS; x++) {
                for (int y = 0; y < Game.BOARD_ROWS; y++) {
                    map[x][y] = new CellInfo(x, y);
                }
            }

            printBoard();
            state = CliState.GAME;
        } else if (event instanceof PlayerTurnStartEvent) {
            availableCommands.clear();

            turn = ((PlayerTurnStartEvent) event).getPlayer().equals(player);
        } else if (event instanceof AbstractRequestEvent) {
            if (sent) {
                availableCommands.clear();
                sent = false;
            }

            if (event instanceof RequestWorkerSpawnEvent) {
                availableCommands.add(new Command("spawn", "x:y",
                        args -> {
                            if (args.length != 2) {
                                return false;
                            }

                            Coordinates coordinates = coordinatesOrNull(args);

                            if (coordinates == null) {
                                return false;
                            }

                            client.send(serializer.serialize(new WorkerSpawnEvent(player, -1, coordinates)));
                            return true;
                        }
                ));
            } else if (event instanceof RequestWorkerMoveEvent) {
                int worker = ((RequestWorkerMoveEvent) event).getWorker();

                availableCommands.add(new Command("move" + worker, "x:y",
                        args -> {
                            if (args.length != 2) {
                                return false;
                            }

                            Coordinates coordinates = coordinatesOrNull(args);

                            if (coordinates == null) {
                                return false;
                            }

                            client.send(serializer.serialize(new WorkerMoveEvent(player, worker, coordinates)));
                            return true;
                        }
                ));
            } else if (event instanceof RequestWorkerBuildBlockEvent) {
                int worker = ((RequestWorkerBuildBlockEvent) event).getWorker();

                availableCommands.add(new Command("block" + worker, "x:y",
                        args -> {
                            if (args.length != 2) {
                                return false;
                            }

                            Coordinates coordinates = coordinatesOrNull(args);

                            if (coordinates == null) {
                                return false;
                            }

                            client.send(serializer.serialize(new WorkerBuildBlockEvent(player, worker, coordinates)));
                            return true;
                        }
                ));
            } else if (event instanceof RequestWorkerBuildDomeEvent) {
                int worker = ((RequestWorkerBuildDomeEvent) event).getWorker();

                availableCommands.add(new Command("dome" + worker, "x:y",
                        args -> {
                            if (args.length != 2) {
                                return false;
                            }

                            Coordinates coordinates = coordinatesOrNull(args);

                            if (coordinates == null) {
                                return false;
                            }

                            client.send(serializer.serialize(new WorkerBuildDomeEvent(player, worker, coordinates)));
                            return true;
                        }
                ));
            } else if (event instanceof RequestPlayerEndTurnEvent) {
                if (((RequestPlayerEndTurnEvent) event).getCanBeEnded()) {
                    availableCommands.add(new Command("end", null,
                            args -> {
                                if (args.length != 0) {
                                    return false;
                                }

                                client.send(serializer.serialize(new PlayerEndTurnEvent(player)));
                                return true;
                            }
                    ));
                }
            }

            turn = true;
            printBoard();
        } else {
            availableCommands.clear();

            if (event instanceof WorkerSpawnEvent) {
                WorkerSpawnEvent workerSpawnEvent = (WorkerSpawnEvent) event;

                workers.put(workerSpawnEvent.getId(), new WorkerInfo(workerSpawnEvent.getId(), workerSpawnEvent.getPlayer(), workerSpawnEvent.getPosition()));
                printBoard();
            } else if (event instanceof WorkerMoveEvent) {
                WorkerMoveEvent workerMoveEvent = (WorkerMoveEvent) event;

                workers.get(workerMoveEvent.getId()).setPosition(((WorkerMoveEvent) event).getDestination());
                printBoard();
            } else if (event instanceof WorkerBuildBlockEvent) {
                WorkerBuildBlockEvent workerBuildBlockEvent = (WorkerBuildBlockEvent) event;

                CellInfo cellInfo = getCellInfo(workerBuildBlockEvent.getDestination());
                cellInfo.setLevel(cellInfo.getLevel() + 1);
                printBoard();
            } else if (event instanceof WorkerBuildDomeEvent) {
                WorkerBuildDomeEvent workerBuildDomeEvent = (WorkerBuildDomeEvent) event;

                CellInfo cellInfo = getCellInfo(workerBuildDomeEvent.getDestination());
                cellInfo.setDoomed(true);
                printBoard();
            } else if (event instanceof PlayerLoseEvent) {
                if (((PlayerLoseEvent) event).getPlayer().equals(player)) {
                    state = CliState.SPECTATOR;
                    printBoard();
                }
            } else if (event instanceof PlayerWinEvent) {
                PlayerWinEvent playerWinEvent = (PlayerWinEvent) event;

                winner = playerWinEvent.getPlayer();
                state = CliState.END;
                printEnd();
            }
        }

        forwarded = true;
    }

    private void onError(ErrorMessage message) {
        invalid = true;
        out("Unexpected error while sending message, try again");
    }

    private void printLobby() {
        out(CLEAR);
        out("> Logged in as " + player);
        out(SEPARATOR_HIGH);
        for (RoomInfo roomInfo : rooms) {
            int count = getRoomCount(roomInfo);
            int max = roomInfo.getMaxPlayers();
            String simple = roomInfo.isSimpleGame() ? "Yes" : "No";

            out("# Name: " + roomInfo.getOwner() + " (Players: " + count + "/" + max + ", Simple: " + simple + ")");
            out(SEPARATOR_LOW);
        }
        out("Type \"" + COMMAND_CREATE +":max-players:simple|normal\" to create a new room, or type \"" + COMMAND_JOIN + ":room-name\" to join a room");
        flush();
    }

    private void printRoom() {
        int count = getRoomCount(room);
        int max = room.getMaxPlayers();

        out(CLEAR);
        out(SEPARATOR_HIGH);
        out("You are in the room: " + room.getOwner());
        out("Players (" + count +"/" + max + "): " + String.join(", ", room.getOtherPlayers()));
        out("The game will start as soon as the room is filled");
        out(SEPARATOR_HIGH);
        flush();
    }

    private void printBoard() {
        out(CLEAR);
        out(SEPARATOR_HIGH);
        StringBuilder boardBuilder = new StringBuilder();
        // Scan top to bottom to easily print in the correct order
        for (int y = Game.BOARD_ROWS - 1; y >= 0; y--) {
            for (int x = 0; x < Game.BOARD_COLUMNS; x++) {
                CellInfo cellInfo = getCellInfo(new Coordinates(x, y));
                WorkerInfo workerInfo = getWorkerByCoords(x, y);

                boardBuilder.append(" ".repeat(SPACING / 2 - cellInfo.getLevel()));
                boardBuilder.append("|".repeat(cellInfo.getLevel()));

                if (cellInfo.isDoomed()) {
                    boardBuilder.append("X");
                } else if (workerInfo != null) {
                    boardBuilder.append(workerInfo.id);
                } else {
                    boardBuilder.append("-");
                }

                boardBuilder.append("|".repeat(cellInfo.getLevel()));
                boardBuilder.append(" ".repeat(SPACING / 2 - cellInfo.getLevel()));
            }

            if (y == 3) {
                boardBuilder.append(" ".repeat(SPACING * 2)).append("Your workers: ");
            }

            if (y == 2) {
                List<String> workerIds = new ArrayList<>();
                for (WorkerInfo workerInfo : workers.values()) {
                    if (workerInfo.getOwner().equals(player)) {
                        workerIds.add(workerInfo.getId() + "");
                    }
                }

                boardBuilder.append(" ".repeat(SPACING * 2));

                if (workerIds.size() == 0) {
                    boardBuilder.append("No workers spawned");
                } else {
                    boardBuilder.append(String.join(", ", workerIds));
                }
            }

            boardBuilder.append(System.lineSeparator());
            if (y > 0) {
                boardBuilder.append(" ".repeat(SPACING * Game.BOARD_COLUMNS)).append(System.lineSeparator());
            }
        }
        boardBuilder.append(SEPARATOR_HIGH).append(System.lineSeparator());

        if (state == CliState.GAME) {
            if (availableCommands.size() > 0) {
                boardBuilder.append("# Available commands:");
                for (Command command : availableCommands) {
                    boardBuilder.append(System.lineSeparator()).append(command.getSyntax());
                }
            } else {
                boardBuilder.append("# Waiting for your turn...");
            }

            out(boardBuilder.toString());
            flush();
        } else if (state == CliState.SPECTATOR) {
            boardBuilder.append("# You have lost the game and are now spectating");

            out(boardBuilder.toString());
            flush();
        } else {
            out(boardBuilder.toString());
        }
    }

    private void printEnd() {
        printBoard();
        if (winner.equals(player)) {
            out(" ".repeat(SPACING) + "Congratulations! You have won the game!");
        } else {
            out(" ".repeat(SPACING) + winner + " has won the game");
        }
        out(SEPARATOR_HIGH);
        flush();
    }

    private WorkerInfo getWorkerByCoords(int x, int y) {
        for (WorkerInfo other : workers.values()) {
            if (other.position.equals(new Coordinates(x, y))) {
                return other;
            }
        }

        return null;
    }

    private void out(String message) {
        output.add(message);
    }

    private void flush() {
        if (output.size() <= 0) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(output.get(0));
        for (String line : output.subList(1, output.size())) {
            builder.append(System.lineSeparator());
            builder.append(line);
        }
        output.clear();

        Logger.getInstance().info(builder.toString());
    }

    private String in(String request) {
        waiting = true;
        out(request);
        flush();
        String input = scanner.next();

        if (input.equals(COMMAND_STOP)) {
            shutdown();
            System.exit(0);
            return "";
        }

        waiting = false;
        return input;
    }

    private int inInt(String request) {
        waiting = true;

        while (waiting) {
            out(request);
            flush();

            if (!scanner.hasNextInt()) {
                scanner.next();
                out("Invalid value, please insert a number");
                continue;
            }

            int result = scanner.nextInt();
            waiting = false;
            return result;
        }

        return -1;
    }

    private Integer intOrNull(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private Coordinates coordinatesOrNull(String[] args) {
        Integer x = intOrNull(args[0]);
        Integer y = intOrNull(args[1]);

        if (x == null || y == null) {
            return null;
        }

        return new Coordinates(x, y);
    }

    private int getRoomCount(RoomInfo info) {
        return info.getOtherPlayers().size() + 1;
    }

    private CellInfo getCellInfo(Coordinates coordinates) {
        return map[coordinates.getX()][coordinates.getY()];
    }

}
