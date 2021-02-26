package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.event.PlayerTurnStartEvent;
import it.polimi.ingsw.common.event.WorkerSpawnEvent;
import it.polimi.ingsw.common.event.request.RequestWorkerSpawnEvent;
import it.polimi.ingsw.model.*;

import java.util.*;

/**
 * The class representing the state of game in which the players choose where to spawn their workers and spans them
 */
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
     * The counter starts at 0
     */
    private int nextWorkerId;

    /**
     * Class constructor
     * @param provider The provider of the events
     * @param board The game's board
     * @param players The game's players
     * @param maxWorkers The max number of worker each player can spawn
     */
    public PreWorkersGame(ModelEventProvider provider, Board board, List<Player> players, int maxWorkers) {
        this(provider, board, players, maxWorkers, false);
    }

    /**
     * Class constructor
     * @param provider The provider of the events
     * @param board The game's board
     * @param players The game's players
     * @param maxWorkers The max number of worker each player can spawn
     * @param alreadySorted True if player's are ordered by age. If false the list of
     *                      player is sorted.
     */
    public PreWorkersGame(ModelEventProvider provider, Board board, List<Player> players, int maxWorkers, boolean alreadySorted) {
        super(provider, board, players);

        this.maxWorkers = maxWorkers;

        if (!alreadySorted) {
            List<Player> sortedPlayers = new ArrayList<>(getPlayers());
            sortedPlayers.sort(Comparator.comparingInt(Player::getAge));
            sortPlayers(sortedPlayers);
        }

        var event = new PlayerTurnStartEvent(getCurrentPlayer().getName());
        setReceivers(event);
        event.accept(getModelEventProvider());

        new RequestWorkerSpawnEvent(getCurrentPlayer().getName(), getAvailablePositions())
                .accept(getModelEventProvider());
    }

    /**
     * @see AbstractGameState#spawnWorker(Coordinates)
     */
    @Override
    public ModelResponse spawnWorker(Coordinates position) {
        if (!getAvailablePositions().contains(position)) {
            // Invalid cell selected
            return ModelResponse.INVALID_PARAMS;
        }

        Cell cell = getBoard().getCell(position);
        Worker worker = new Worker(nextWorkerId, cell);
        nextWorkerId++;

        Player player = getCurrentPlayer();
        player.addWorker(worker);

        var event = new WorkerSpawnEvent(player.getName(), worker.getId(), toCoordinates(cell));
        setReceivers(event);
        event.accept(getModelEventProvider());

        Player next = getCurrentPlayer();

        if (next != null && next.equals(player)) {
            // Notify the current player that a new worker can be placed
            new RequestWorkerSpawnEvent(player.getName(), getAvailablePositions())
                    .accept(getModelEventProvider());
        }

        return ModelResponse.ALLOW;
    }

    /**
     * @see AbstractGameState#getCurrentPlayer()
     */
    @Override
    public Player getCurrentPlayer() {
        if (isDone()) {
            return null;
        }

        Player currentPlayer = getPlayers().get(playerIndex);

        if (currentPlayer.getWorkers().size() < maxWorkers) {
            return currentPlayer;
        }

        playerIndex++;
        currentPlayer = getPlayers().get(playerIndex);

        var event = new PlayerTurnStartEvent(currentPlayer.getName());
        setReceivers(event);
        event.accept(getModelEventProvider());

        new RequestWorkerSpawnEvent(currentPlayer.getName(), getAvailablePositions())
                .accept(getModelEventProvider());

        return currentPlayer;
    }

    /**
     * @see AbstractGameState#nextState()
     */
    @Override
    public AbstractGameState nextState() {
        if (!isDone()) {
            return this;
        }

        return new OngoingGame(getModelEventProvider(), getBoard(), getPlayers());
    }
    /**
     * Check if a players has spawned all of his workers
     * @return true if the player has spawned all of his workers
     */
    private boolean isDone() {
        for (Player player : getPlayers()) {
            if (player.getWorkers().size() < maxWorkers) {
                return false;
            }
        }

        return true;
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

    /**
     * Converts a list of cells in their corresponding coordinates
     * @param cells The list of cells
     * @return The list of coordinates
     */
    private List<Coordinates> toCoordinatesList(List<Cell> cells) {
        List<Coordinates> coordinates = new ArrayList<>();

        for (Cell cell : cells) {
            coordinates.add(toCoordinates(cell));
        }

        return coordinates;
    }

    /**
     * Converts a cells in his corresponding coordinates
     * @param cell The selected cell
     * @return The coordinates
     */
    private Coordinates toCoordinates(Cell cell) {
        return new Coordinates(cell.getX(), cell.getY());
    }

}
