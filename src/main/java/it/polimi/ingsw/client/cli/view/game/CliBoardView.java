package it.polimi.ingsw.client.cli.view.game;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.CliConstants;
import it.polimi.ingsw.client.cli.view.AbstractCliView;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.client.data.request.InteractData;
import it.polimi.ingsw.client.data.request.WorkersInteractData;
import it.polimi.ingsw.client.data.request.WorkersOtherInteractData;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.info.WorkerInfo;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Generate the cli view of the board and the responses to commands
 */
public class CliBoardView extends AbstractGameView {

    public static final String COLOR_BACKGROUND = "\u001B[45m";
    public static final String HIGHLIGHT_COLOR = "\u001B[35m";

    private static final String BOARD_INDEX = "  0   1   2   3   4  ";
    private static final String BOARD_ROOF = "┌---┬---┬---┬---┬---┐";
    private static final String BOARD_FLOORS = "├---┼---┼---┼---┼---┤";
    private static final String BOARD_BASEMENT = "└---┴---┴---┴---┴---┘";
    private static final int BOARD_PADDING = 20;
    private static final int BOARD_SPACING = 3;

    private static final String DOME = "☼";

    private static final String WORKER1 = "⚒";
    private static final String WORKER2 = "⛏";
    private static final String WORKER3 = "⚔";
    private static final String WORKER4 = "☭";

    private static final String POSSIBLE_INTERACTION = "⚐";

    private static final int SPACING = 8;

    private static final String COMMAND_FAIL = "Invalid command, please try again";
    private static final String SPAWN_FAIL = "Please, type the coordinates of the cell chosen (Example: spawn 1 2)";
    private static final String MOVE_FAIL = "Please, type the number of the worker and the coordinates of the cell chosen (Example: move 1 1 2)";
    private static final String BLOCK_FAIL = "Please, type the number of the worker and the coordinates of the cell chosen (Example: block 1 4 0)";
    private static final String DOME_FAIL = "Please, type the number of the worker and the coordinates of the cell chosen (Example: dome 0 3 1)";
    private static final String FORCE_FAIL = "Please, type the numbers of the forcer and forced workers and the coordinates of the cell chosen (Example: force 0 2 2 2)";
    private static final String END_FAIL = "Please, just type end";

    /**
     * The related state
     */
    private final GameState state;

    /**
     * Class constructor, generate a board view given client state and line length
     *
     * @param state The state
     * @param lineLength The line length
     */
    public CliBoardView(GameState state, int lineLength) {
        super(state, lineLength);

        this.state = super.getState();
    }

    /**
     * @see AbstractCliView#generateView()
     */
    @Override
    public String generateView() {
        GameData data = state.getData();

        StringBuilder output = new StringBuilder();

        output.append(separator()).append(System.lineSeparator().repeat(BOARD_SPACING));

        output.append(" ".repeat(BOARD_PADDING)).append(BOARD_ROOF).append(System.lineSeparator());

        List<String> otherPlayers = new ArrayList<>(data.getOtherPlayers());
        if (data.getTurnPlayer().isPresent() && !otherPlayers.contains(data.getTurnPlayer().get())) {
            otherPlayers.add(data.getTurnPlayer().get());
        }
        otherPlayers.remove(data.getName());
        // Scan top to bottom to easily print in the correct order
        for (int y = data.getMap()[0].length - 1; y >= 0; y--) {

            output.append(" ".repeat(BOARD_PADDING-2)).append(y).append(" ").append("|");
            for (int x = 0; x < data.getMap().length; x++) {
                CellInfo cellInfo = data.getMap()[x][y];
                WorkerInfo workerInfo = getWorkerByCoords(x, y);
                Coordinates coords = new Coordinates(x, y);

                output.append(getBlocksChar(cellInfo.getLevel()));
                if (cellInfo.isDoomed()) {
                    output.append(DOME);
                } else if ((data.getMoveData().isPresent()
                        && data.getMoveData().get().getAvailableInteractions().values().stream().anyMatch(interactData -> interactData.getAvailableCoordinates().contains(coords)))
                        || (data.getBuildBlockData().isPresent()
                        && data.getBuildBlockData().get().getAvailableInteractions().values().stream().anyMatch(interactData -> interactData.getAvailableCoordinates().contains(coords)))
                        || (data.getBuildDomeData().isPresent()
                        && data.getBuildDomeData().get().getAvailableInteractions().values().stream().anyMatch(interactData -> interactData.getAvailableCoordinates().contains(coords)))
                        || (data.getForceData().isPresent()
                        && data.getForceData().get().getAvailableOtherInteractions().values().stream().anyMatch(
                            interact -> interact.getAvailableInteractions().values().stream().anyMatch(interactData -> interactData.getAvailableCoordinates().contains(coords))))
                ) {
                    if (workerInfo != null) {
                        output.append(COLOR_BACKGROUND).append(getPawn(workerInfo.getId())).append(CliConstants.RESET);
                    }
                    else {
                        output.append(HIGHLIGHT_COLOR).append(POSSIBLE_INTERACTION).append(CliConstants.RESET);
                    }
                } else if (workerInfo != null) {
                    int workersPerPlayer = Game.MAX_WORKERS;
                    CliConstants.CliColor color = CliConstants.PLAYER_COLORS[(workerInfo.getId() / workersPerPlayer) % CliConstants.PLAYER_COLORS.length];

                    output.append(color.getBackground()).append(getPawn(workerInfo.getId())).append(CliConstants.RESET);
                } else {
                    output.append(" ");
                }
                output.append(getBlocksChar(cellInfo.getLevel()));
                output.append("|");
            }

            if (y == 3) {
                output.append(" ".repeat(SPACING * 2)).append("Your workers(").append(getPlayersPawn(data.getName())).append(") : ");

                if (!hasSpawned()) {
                    output.append("No workers spawned");
                } else {
                    for (WorkerInfo worker : data.getWorkers()) {
                        if (worker.getOwner().equals(data.getName())) {
                            output.append("Worker").append(worker.getId()).append(" (").append(worker.getPosition().toString()).append(") ");
                        }
                    }
                }
            }

            if (y <= 2 && otherPlayers.size()>0) {

                output.append(" ".repeat(SPACING * 2)).append(otherPlayers.get(0)).append("'s workers(").append(getPlayersPawn(otherPlayers.get(0))).append(") : ");

                boolean hasWorkers = false;
                for (WorkerInfo worker : data.getWorkers()) {
                    if (worker.getOwner().equals(otherPlayers.get(0))) {
                        output.append("Worker").append(worker.getId()).append(" (").append(worker.getPosition().toString()).append(") ");
                        hasWorkers = true;
                    }
                }

                if (!hasWorkers) {
                    output.append("No workers spawned");
                }

                otherPlayers.remove(0);
            }

            output.append(System.lineSeparator());

            output.append(" ".repeat(BOARD_PADDING));
            if (y != 0) {
                output.append(BOARD_FLOORS).append(System.lineSeparator());
            } else {
                output.append(BOARD_BASEMENT).append(System.lineSeparator());
                output.append(" ".repeat(BOARD_PADDING)).append(BOARD_INDEX).append(System.lineSeparator());
            }
        }

        output.append(System.lineSeparator().repeat(BOARD_SPACING));

        output.append(separator()).append(System.lineSeparator());

        Optional<String> lastMessage = data.getLastMessage();
        lastMessage.ifPresent(string -> output.append(center(string)).append(System.lineSeparator()));

        return output.toString();
    }

    /**
     * @see AbstractCliView#generateCommands()
     */
    @Override
    public List<CliCommand> generateCommands() {
        List<CliCommand> commands = new ArrayList<>();

        if (state.getData().getTurnPlayer().isPresent() &&
                state.getData().getTurnPlayer().get().equals(state.getData().getName()) &&
                !state.getData().isSpectating()) {
            if (getPlayerWorkersNumber() < 2) {
                return List.of(
                        new CliCommand("spawn", new String[]{"<x>", "<y>"}, "Spawn your worker", this::onSpawn)
                );
            }

            if (state.getData().getMoveData().isPresent()) {
                commands.add(new CliCommand("move", new String[]{"<worker>", "<x>", "<y>"}, "Move one of your workers", this::onMove));
            }
            if (state.getData().getBuildBlockData().isPresent()) {
                commands.add(new CliCommand("block", new String[]{"<worker>", "<x>", "<y>"}, "Build a block ", this::onBuildBlock));
            }
            if (state.getData().getBuildDomeData().isPresent()) {
                commands.add(new CliCommand("dome", new String[]{"<worker>", "<x>", "<y>"}, "Dome ", this::onBuildDome));
            }
            if (state.getData().getForceData().isPresent()) {
                commands.add(new CliCommand("force", new String[]{"<worker>", "<forced worker>", "<x>", "<y>"}, "Force an opponent's worker in another cell", this::onForce));
            }
            if (state.getData().getCanBeEnded().isPresent() && state.getData().getCanBeEnded().get()) {
                commands.add(new CliCommand("end", new String[]{}, "End ", this::onEndTurn));
            }
        }

        return commands;
    }

    /**
     * Respond to a spawn command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onSpawn(String[] arguments) {
        GameData data = state.getData();

        Optional<InteractData> interactData = data.getSpawnData();

        if (interactData.isEmpty()) {
            return Optional.of(COMMAND_FAIL);
        }

        if (arguments.length != 2) {
            return Optional.of(SPAWN_FAIL);
        }

        try {
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);

            Coordinates coordinates = new Coordinates(x, y);

            if (!interactData.get().getAvailableCoordinates().contains(coordinates)) {
                return Optional.of("The worker can't spawn there, please choose a valid position\nThe worker can spawn everywhere except on top of another worker");
            }

            state.acceptSpawn(x, y);
        } catch (NumberFormatException e) {
            return Optional.of(SPAWN_FAIL);
        }

        return Optional.empty();
    }

    /**
     * Respond to a move command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onMove(String[] arguments) {
        GameData data = state.getData();

        Optional<WorkersInteractData> workersInteractData = data.getMoveData();

        if (workersInteractData.isEmpty()) {
            return Optional.of(COMMAND_FAIL);
        }

        if (arguments.length != 3) {
            return Optional.of(MOVE_FAIL);
        }

        try {
            int worker = Integer.parseInt(arguments[0]);
            int x = Integer.parseInt(arguments[1]);
            int y = Integer.parseInt(arguments[2]);

            if (!isCorrectWorker(worker)) {
                return Optional.of("You cannot move another player's worker");
            }

            InteractData interactData = workersInteractData.get().getAvailableInteractions().get(worker);

            if (interactData == null || interactData.getAvailableCoordinates().size() <= 0) {
                String available = getReadableCanInteract(workersInteractData.get());
                return Optional.of("The worker can't move, you must use another one\nWorkers that can move: " + available);
            }

            if (!interactData.getAvailableCoordinates().contains(new Coordinates(x, y))) {
                String available = getReadablePositions(interactData);
                return Optional.of("The worker can't move there, please choose a valid position\nAvailable positions: " + available);
            }

            state.acceptMove(worker, x, y);

        } catch (NumberFormatException e) {
            return Optional.of(MOVE_FAIL);
        }

        return Optional.empty();
    }

    /**
     * Respond to a build block command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onBuildBlock(String[] arguments) {
        GameData data = state.getData();

        Optional<WorkersInteractData> workersInteractData = data.getBuildBlockData();

        if (workersInteractData.isEmpty()) {
            return Optional.of(COMMAND_FAIL);
        }

        if (arguments.length != 3) {
            return Optional.of(BLOCK_FAIL);
        }

        try {
            int worker = Integer.parseInt(arguments[0]);
            int x = Integer.parseInt(arguments[1]);
            int y = Integer.parseInt(arguments[2]);

            if (!isCorrectWorker(worker)) {
                return Optional.of("You cannot build a block with another player's worker");
            }

            InteractData interactData = workersInteractData.get().getAvailableInteractions().get(worker);

            if (interactData == null || interactData.getAvailableCoordinates().size() <= 0) {
                String available = getReadableCanInteract(workersInteractData.get());
                return Optional.of("The worker can't build a block, you must use another one\nWorkers that can build a block: " + available);
            }

            if (!interactData.getAvailableCoordinates().contains(new Coordinates(x, y))) {
                String available = getReadablePositions(interactData);
                return Optional.of("The worker can't build a block there, please choose a valid position\nAvailable positions: " + available);
            }

            state.acceptBuildBlock(worker, x, y);

        } catch (NumberFormatException e) {
            return Optional.of(BLOCK_FAIL);
        }

        return Optional.empty();
    }

    /**
     * Respond to a build dome command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onBuildDome(String[] arguments) {
        GameData data = state.getData();

        Optional<WorkersInteractData> workersInteractData = data.getBuildDomeData();

        if (workersInteractData.isEmpty()) {
            return Optional.of(COMMAND_FAIL);
        }

        if (arguments.length != 3) {
            return Optional.of(DOME_FAIL);
        }

        try {
            int worker = Integer.parseInt(arguments[0]);
            int x = Integer.parseInt(arguments[1]);
            int y = Integer.parseInt(arguments[2]);

            if (!isCorrectWorker(worker)) {
                return Optional.of("You cannot build a dome with another player's worker");
            }

            InteractData interactData = workersInteractData.get().getAvailableInteractions().get(worker);

            if (interactData == null || interactData.getAvailableCoordinates().size() <= 0) {
                String available = getReadableCanInteract(workersInteractData.get());
                return Optional.of("The worker can't build a dome, you must use another one\nWorkers that can build a block: " + available);
            }

            if (!interactData.getAvailableCoordinates().contains(new Coordinates(x, y))) {
                String available = getReadablePositions(interactData);
                return Optional.of("The worker can't build a dome there, please choose a valid position\nAvailable positions: " + available);
            }

            state.acceptBuildDome(worker, x, y);

        } catch (NumberFormatException e) {
            return Optional.of(DOME_FAIL);
        }

        return Optional.empty();
    }

    /**
     * Respond to a force command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onForce(String[] arguments) {
        GameData data = state.getData();

        Optional<WorkersOtherInteractData> workersOtherInteractData = data.getForceData();

        if (workersOtherInteractData.isEmpty()) {
            return Optional.of(COMMAND_FAIL);
        }

        if (arguments.length != 4) {
            return Optional.of(FORCE_FAIL);
        }

        try {
            int forcer = Integer.parseInt(arguments[0]);
            int forced = Integer.parseInt(arguments[1]);
            int x = Integer.parseInt(arguments[2]);
            int y = Integer.parseInt(arguments[3]);

            if (!isCorrectWorker(forcer)) {
                return Optional.of("You cannot force using another player's worker");
            }

            WorkersInteractData workersInteractData = workersOtherInteractData.get().getAvailableOtherInteractions().get(forcer);

            if (workersInteractData == null || workersInteractData.getAvailableInteractions().size() == 0) {
                String available = workersOtherInteractData.get().getAvailableOtherInteractions().entrySet().stream()
                        .filter(entry -> entry.getValue() != null && entry.getValue().getAvailableInteractions().size() > 0)
                        .map(entry -> "#" + entry.getKey())
                        .collect(Collectors.joining(", "));
                return Optional.of("The worker #" + forcer + " can't force this turn\nWorkers that can force: " + available);
            }

            InteractData interactData = workersInteractData.getAvailableInteractions().get(forced);

            if (interactData == null || interactData.getAvailableCoordinates().size() == 0) {
                String available = getReadableCanInteract(workersInteractData);
                return Optional.of("The worker #" + forcer + " can't force the worker #" + forced + "\nWorkers that can be forced: " + available);
            }

            if (!interactData.getAvailableCoordinates().contains(new Coordinates(x, y))) {
                String available = getReadablePositions(interactData);
                return Optional.of("The worker #" + forcer + " can't force the worker #" + forced + " in that position\nAvailable positions: " + available);
            }

            if (!isCorrectWorker(forcer) || isCorrectWorker(forced)) {
                return Optional.of(FORCE_FAIL);
            }

            state.acceptForce(forcer, forced, x, y);

        } catch (NumberFormatException e) {
            return Optional.of(FORCE_FAIL);
        }

        return Optional.empty();
    }

    /**
     * Respond to an end of turn command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onEndTurn(String[] arguments) {
        if (arguments.length != 0) {
            return Optional.of(END_FAIL);
        }

        state.acceptEnd();
        return Optional.empty();
    }

    /**
     * Get the worker's pawn
     * @param workerNumber The worker ID number
     * @return The pawn
     */
    private String getPawn(int workerNumber) {
        if (workerNumber < 2) {
            return WORKER1;
        }
        if (workerNumber < 4) {
            return WORKER2;
        }
        if (workerNumber < 6) {
            return WORKER3;
        }
        return WORKER4;
    }

    /**
     * Get the pawn of a player workers
     * @param player The player
     * @return The pawn
     */
    private String getPlayersPawn(String player) {
        for (WorkerInfo worker : state.getData().getWorkers()) {
            if (worker.getOwner().equals(player)) {
                return getPawn(worker.getId());
            }
        }
        return " ";
    }

    /**
     * Verifies if the player has already spawned his workers
     * @return True if the player has already spawned his workers, false if not
     */
    private boolean hasSpawned() {
        for (WorkerInfo worker : state.getData().getWorkers()) {
            if (worker.getOwner().equals(state.getData().getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the char correspondent to a cell's height
     * @param n The number of blocks on a cell
     * @return The char
     */
    private String getBlocksChar (int n) {
        if (n==3)
            return "⦀";
        if (n==2)
            return "‖";
        if (n==1)
            return "|";
        return " ";
    }

    /**
     * Get a worker given his coordinate
     * @param x The x index
     * @param y The y index
     * @return The worker
     */
    private WorkerInfo getWorkerByCoords(int x, int y) {
        for (WorkerInfo worker : state.getData().getWorkers()) {
            if (worker.getPosition().equals(new Coordinates(x, y))) {
                return worker;
            }
        }
        return null;
    }

    /**
     * Get how many workers the player has on the board
     * @return The workers' number
     */
    private int getPlayerWorkersNumber() {
        int i=0;
        for (WorkerInfo worker : state.getData().getWorkers()) {
            if (worker.getOwner().equals(state.getData().getName())) {
                i++;
            }
        }
        return i;
    }

    /**
     * Check whether or not the worker is a player's one
     * @param workerID The worker's ID
     * @return True if the worker is a player's one, false if not
     */
    private boolean isCorrectWorker(int workerID) {
        for (WorkerInfo worker : state.getData().getWorkers()) {
            if (workerID == worker.getId()) {
                return worker.getOwner().equals(state.getData().getName());
            }
        }
        return false;
    }

    /**
     * Get a readable list of workers that can interact
     * @param data The workers interaction data
     * @return The readable string
     */
    private String getReadableCanInteract(WorkersInteractData data) {
        return data.getAvailableInteractions().entrySet().stream()
                .filter(entry -> entry.getValue() != null && entry.getValue().getAvailableCoordinates().size() > 0)
                .map(entry -> "#" + entry.getKey())
                .collect(Collectors.joining(", "));
    }

    /**
     * Get a readable list of positions where a worker can interact
     * @param data The interaction data
     * @return The readable string
     */
    private String getReadablePositions(InteractData data) {
        return data.getAvailableCoordinates().stream()
                .map(coordinates -> "[" + coordinates.getX() + " " + coordinates.getY() + "]")
                .collect(Collectors.joining(", "));
    }

}
