package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.events.PlayerTurnStartEvent;
import it.polimi.ingsw.common.events.WorkerSpawnEvent;
import it.polimi.ingsw.common.events.requests.RequestWorkerSpawnEvent;
import it.polimi.ingsw.model.*;

import java.util.*;

public class PreWorkersGame extends AbstractGameState {

    /**
     * The maximum number of workers per player
     */
    private final int maxWorkers;

    /**
     * The current player index (refers to getPlayers())
     */
    private int playerIndex;

    /**
     * The next worker id
     * Each worker must have a unique id
     */
    private int nextWorkerId;

    public PreWorkersGame(Board board, List<Player> players, int maxWorkers) {
        this(board, players, maxWorkers, false);
    }

    public PreWorkersGame(Board board, List<Player> players, int maxWorkers, boolean alreadySorted) {
        super(board, players);

        this.maxWorkers = maxWorkers;

        if (!alreadySorted) {
            List<Player> sortedPlayers = new LinkedList<>(getPlayers());
            sortedPlayers.sort(Comparator.comparingInt(Player::getAge));
            sortPlayers(sortedPlayers);
        }

        getPlayerTurnStartEventObservable().notifyObservers(new PlayerTurnStartEvent(getCurrentPlayer().getName()));
        getRequestWorkerSpawnEventObservable().notifyObservers(new RequestWorkerSpawnEvent(getAvailablePositions()));
    }

    @Override
    public Game.ModelResponse spawnWorker(Coordinates position) {
        if (!getAvailablePositions().contains(position)) {
            // Invalid cell selected
            return Game.ModelResponse.INVALID_PARAMS;
        }


        Cell cell = getBoard().getCell(position);
        Worker worker = new Worker(nextWorkerId, cell);
        nextWorkerId++;

        Player player = getCurrentPlayer();
        player.addWorker(worker);

        getWorkerSpawnEventObservable().notifyObservers(new WorkerSpawnEvent(worker.getId(), player.getName()));

        Player next = getCurrentPlayer();
        if (next.equals(player)) {
            // Notify the current player that a new worker can be placed
            getRequestWorkerSpawnEventObservable().notifyObservers(new RequestWorkerSpawnEvent(getAvailablePositions()));
        }

        return Game.ModelResponse.ALLOW;
    }

    @Override
    public Player getCurrentPlayer() {
        Player currentPlayer = getPlayers().get(playerIndex);

        if (currentPlayer.getWorkers().size() < maxWorkers) {
            return currentPlayer;
        }

        playerIndex++;
        currentPlayer = getPlayers().get(playerIndex);

        getPlayerTurnStartEventObservable().notifyObservers(new PlayerTurnStartEvent(currentPlayer.getName()));
        getRequestWorkerSpawnEventObservable().notifyObservers(new RequestWorkerSpawnEvent(getAvailablePositions()));
        return currentPlayer;
    }

    @Override
    public AbstractGameState nextState() {
        for (Player player : getPlayers()) {
            if (player.getWorkers().size() < maxWorkers) {
                return this;
            }
        }

        return new OngoingGame(getBoard(), getPlayers());
    }

    /**
     * The list of available cells where a new Worker can be placed
     * @return The list of cells
     */
    private List<Coordinates> getAvailablePositions() {
        List<Cell> cells = getBoard().getCells();

        for (Player player : getPlayers()) {
            for (Worker other : player.getWorkers()) {
                cells.remove(other.getCell());
            }
        }

        return toCoordinatesList(cells);
    }

    private List<Coordinates> toCoordinatesList(List<Cell> cells) {
        List<Coordinates> coordinates = new ArrayList<>();

        for (Cell cell : cells) {
            coordinates.add(new Coordinates(cell.getX(), cell.getY()));
        }

        return coordinates;
    }

}
