package it.polimi.ingsw.client.cli.view.game;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.info.WorkerInfo;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CliBoardView extends AbstractGameView {

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

    private static final String SPAWN_FAIL = "Please, type the coordinates of the cell chosen (Example: spawn 1 2)";
    private static final String MOVE_FAIL = "Please, type the number of the worker and the coordinates of the cell chosen (Example: move 1 1 2)";
    private static final String BLOCK_FAIL = "Please, type the number of the worker and the coordinates of the cell chosen (Example: block 1 4 0)";
    private static final String DOME_FAIL = "Please, type the number of the worker and the coordinates of the cell chosen (Example: dome 0 3 1)";
    private static final String FORCE_FAIL = "Please, type the numbers of the forcer and forced workers and the coordinates of the cell chosen (Example: force 0 2 2 2)";
    private static final String END_FAIL = "Please, just type end";

    private final GameState state;

    public CliBoardView(GameState state, int lineLength) {
        super(state, lineLength);

        this.state = super.getState();
    }

    @Override
    public String generateView() {

        StringBuilder output = new StringBuilder();

        output.append(separator()).append(System.lineSeparator().repeat(BOARD_SPACING));

        output.append(" ".repeat(BOARD_PADDING)).append(BOARD_ROOF).append(System.lineSeparator());

        // Scan top to bottom to easily print in the correct order
        for (int y = Game.BOARD_ROWS - 1; y >= 0; y--) {

            output.append(" ".repeat(BOARD_PADDING)).append("|");
            for (int x = 0; x < Game.BOARD_COLUMNS; x++) {
                CellInfo cellInfo = state.getData().getCellInfo(x, y).get();
                WorkerInfo workerInfo = getWorkerByCoords(x, y);
                Coordinates coords = new Coordinates(x, y);

                output.append(getBlocksChar(cellInfo.getLevel()));
                if (cellInfo.isDoomed()) {
                    output.append(DOME);
                } else if (workerInfo != null) {
                    output.append(getPawn(workerInfo.getId()));
                } else if ((state.getData().getMoveData().isPresent()
                        && state.getData().getMoveData().get().getAvailableInteractions().values().stream().anyMatch(data -> data.getAvailableCoordinates().contains(coords)))
                        || (state.getData().getBuildBlockData().isPresent()
                        && state.getData().getBuildBlockData().get().getAvailableInteractions().values().stream().anyMatch(data -> data.getAvailableCoordinates().contains(coords)))
                        || (state.getData().getBuildDomeData().isPresent()
                        && state.getData().getBuildDomeData().get().getAvailableInteractions().values().stream().anyMatch(data -> data.getAvailableCoordinates().contains(coords)))
                        || (state.getData().getForceData().isPresent()
                        && state.getData().getForceData().get().getAvailableOtherInteractions().values().stream().anyMatch(data -> data.getAvailableInteractions().values().contains(coords)))
                ) {
                    output.append(POSSIBLE_INTERACTION);
                } else {
                    output.append(" ");
                }
                output.append(getBlocksChar(cellInfo.getLevel()));
                output.append("|");
            }

            if (y == 3) {
                if (!hasSpawned()) {
                    output.append(" ".repeat(SPACING * 2)).append("No workers spawned");
                } else {
                    output.append(" ".repeat(SPACING * 2)).append("Your workers: ").append(getPlayersPawn());
                }
            }

            if (y == 2 && hasSpawned()) {
                for (WorkerInfo worker : state.getData().getWorkers()) {
                    if (worker.getOwner().equals(state.getData().getName())) {
                        output.append(" ".repeat(SPACING * 2)).append("Worker ").append(worker.getId()).append(": ").append(worker.getPosition().toString());
                        break;
                    }
                }
            }

            output.append(" ".repeat(SPACING * Game.BOARD_COLUMNS)).append(System.lineSeparator());

            output.append(" ".repeat(BOARD_PADDING));
            if (y != 0) {
                output.append(BOARD_FLOORS);

                if (y == 2 && hasSpawned()) {
                    boolean first=true;
                    for (WorkerInfo worker : state.getData().getWorkers()) {
                        if (worker.getOwner().equals(state.getData().getName())) {

                            if (!first) {
                                output.append(" ".repeat(SPACING * 2)).append("Worker ").append(worker.getId())
                                        .append(": ").append(worker.getPosition().toString());
                            } else {
                                first = false;
                            }
                        }
                    }
                }

                output.append(System.lineSeparator());

            } else {
                output.append(BOARD_BASEMENT).append(System.lineSeparator());
            }
        }

        output.append(System.lineSeparator().repeat(BOARD_SPACING));

        output.append(separator()).append(System.lineSeparator());

        Optional<String> lastMessage = state.getData().getLastMessage();
        lastMessage.ifPresent(string -> output.append(center(string)).append(System.lineSeparator()));

        return output.toString();

    }

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
                commands.add(new CliCommand("force", new String[]{"<worker>", "<x>", "<y>"}, "Force an opponent's worker in another cell", this::onForce));
            }
            if (state.getData().getCanBeEnded().isPresent() && state.getData().getCanBeEnded().get()) {
                commands.add(new CliCommand("end", new String[]{}, "End ", this::onEndTurn));
            }
        }

        return commands;
    }

    private Optional<String> onSpawn(String[] arguments) {
        if (arguments.length != 2) {
            return Optional.of(SPAWN_FAIL);
        }

        try {
            int x = Integer.parseInt(arguments[0]);
            int y = Integer.parseInt(arguments[1]);

            state.acceptSpawn(x, y);
        } catch (NumberFormatException e) {
            return Optional.of(SPAWN_FAIL);
        }

        return Optional.empty();
    }

    private Optional<String> onMove(String[] arguments) {
        if (arguments.length != 3) {
            return Optional.of(MOVE_FAIL);
        }

        try {
            int worker = Integer.parseInt(arguments[0]);
            int x = Integer.parseInt(arguments[1]);
            int y = Integer.parseInt(arguments[2]);

            if (!isCorrectWorker(worker)) {
                return Optional.of(MOVE_FAIL);
            }

            state.acceptMove(worker, x, y);

        } catch (NumberFormatException e) {
            return Optional.of(MOVE_FAIL);
        }

        return Optional.empty();
    }

    private Optional<String> onBuildBlock(String[] arguments) {
        if (arguments.length != 3) {
            return Optional.of(BLOCK_FAIL);
        }

        try {
            int worker = Integer.parseInt(arguments[0]);
            int x = Integer.parseInt(arguments[1]);
            int y = Integer.parseInt(arguments[2]);

            if (!isCorrectWorker(worker)) {
                return Optional.of(BLOCK_FAIL);
            }

            state.acceptBuildBlock(worker, x, y);

        } catch (NumberFormatException e) {
            return Optional.of(BLOCK_FAIL);
        }

        return Optional.empty();
    }

    private Optional<String> onBuildDome(String[] arguments) {
        if (arguments.length != 3) {
            return Optional.of(DOME_FAIL);
        }

        try {
            int worker = Integer.parseInt(arguments[0]);
            int x = Integer.parseInt(arguments[1]);
            int y = Integer.parseInt(arguments[2]);

            if (!isCorrectWorker(worker)) {
                return Optional.of(DOME_FAIL);
            }

            state.acceptBuildDome(worker, x, y);

        } catch (NumberFormatException e) {
            return Optional.of(DOME_FAIL);
        }

        return Optional.empty();
    }

    private Optional<String> onForce(String[] arguments) {
        if (arguments.length != 4) {
            return Optional.of(FORCE_FAIL);
        }

        try {
            int forcer = Integer.parseInt(arguments[0]);
            int forced = Integer.parseInt(arguments[1]);
            int x = Integer.parseInt(arguments[2]);
            int y = Integer.parseInt(arguments[3]);


            if (!isCorrectWorker(forcer) || isCorrectWorker(forced)) {
                return Optional.of(FORCE_FAIL);
            }

            state.acceptForce(forcer, forced, x, y);

        } catch (NumberFormatException e) {
            return Optional.of(FORCE_FAIL);
        }

        return Optional.empty();
    }

    private Optional<String> onEndTurn(String[] arguments) {
        if (arguments.length != 0) {
            return Optional.of(END_FAIL);
        }

        state.acceptEnd();
        return Optional.empty();
    }


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

    private String getPlayersPawn() {
        for (WorkerInfo worker : state.getData().getWorkers()) {
            if (worker.getOwner().equals(state.getData().getName())) {
                return getPawn(worker.getId());
            }
        }
        return "You haven't spawned your workers yet";
    }

    private boolean hasSpawned() {
        for (WorkerInfo worker : state.getData().getWorkers()) {
            if (worker.getOwner().equals(state.getData().getName())) {
                return true;
            }
        }
        return false;
    }

    private String getBlocksChar (int n) {
        if (n==3)
            return "⦀";
        if (n==2)
            return "‖";
        if (n==1)
            return "|";
        return " ";
    }

    private WorkerInfo getWorkerByCoords(int x, int y) {
        for (WorkerInfo worker : state.getData().getWorkers()) {
            if (worker.getPosition().equals(new Coordinates(x, y))) {
                return worker;
            }
        }
        return null;
    }

    private int getPlayerWorkersNumber() {
        int i=0;
        for (WorkerInfo worker : state.getData().getWorkers()) {
            if (worker.getOwner().equals(state.getData().getName())) {
                i++;
            }
        }
        return i;
    }

    private boolean isCorrectWorker(int workerID) {
        for (WorkerInfo worker : state.getData().getWorkers()) {
            if (workerID == worker.getId()) {
                return worker.getOwner().equals(state.getData().getName());
            }
        }
        return false;
    }

}
