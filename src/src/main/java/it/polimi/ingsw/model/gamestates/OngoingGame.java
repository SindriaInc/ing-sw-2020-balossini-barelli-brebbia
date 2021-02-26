package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.request.*;
import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.function.Predicate;

/**
 * The class representing the state of the game in which a player can control his worker and eventually win or lose.
 */
public class OngoingGame extends AbstractGameState {

    /**
     * The current player index (refers to getPlayers())
     */
    private int playerIndex;

    /**
     * The current turn
     * If null, the player has not yet done anything
     */
    private Turn turn;

    /**
     * Class constructor
     * @param provider The provider of the events
     * @param board The game's board
     * @param players The game's players
     */
    public OngoingGame(ModelEventProvider provider, Board board, List<Player> players) {
        super(provider, board, players);

        Player player = getCurrentPlayer();
        var event = new PlayerTurnStartEvent(player.getName());
        setReceivers(event);
        event.accept(getModelEventProvider());
        generateRequests(player);
    }

    /**
     * @see AbstractGameState#moveWorker(int, Coordinates)
     */
    @Override
    public ModelResponse moveWorker(int worker, Coordinates destination) {
        Worker modelWorker = getWorkerById(worker);

        if (modelWorker == null) {
            return ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(modelWorker);

        if (turn.isEmpty()) {
            return ModelResponse.INVALID_PARAMS;
        }

        if (!getAvailableMoves(turn.get()).contains(destination)) {
            return ModelResponse.INVALID_PARAMS;
        }

        Cell cell = getBoard().getCell(destination);
        Player player = getCurrentPlayer();
        player.doMove(turn.get(), cell);
        notifyMovements(player.getName(), turn.get());

        this.turn = turn.get();

        var event = new WorkerMoveEvent(player.getName(), worker, destination);
        setReceivers(event);
        event.accept(getModelEventProvider());

        if (player.checkHasWon(turn.get())) {
            // The current player has won, remove every opponent
            getOpponents(player).forEach(super::removePlayer);
            return ModelResponse.ALLOW;
        }

        generateRequests(getCurrentPlayer());
        return ModelResponse.ALLOW;
    }

    /**
     * @see AbstractGameState#buildBlock(int, Coordinates)
     */
    @Override
    public ModelResponse buildBlock(int worker, Coordinates destination) {
        Worker modelWorker = getWorkerById(worker);

        if (modelWorker == null) {
            return ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(modelWorker);

        if (turn.isEmpty()) {
            return ModelResponse.INVALID_PARAMS;
        }

        if (!getAvailableBlockBuilds(turn.get()).contains(destination)) {
            return ModelResponse.INVALID_PARAMS;
        }

        Cell cell = getBoard().getCell(destination);
        Player player = getCurrentPlayer();
        player.doBuildBlock(turn.get(), cell);
        notifyMovements(player.getName(), turn.get());

        this.turn = turn.get();

        var event = new WorkerBuildBlockEvent(player.getName(), worker, destination);
        setReceivers(event);
        event.accept(getModelEventProvider());

        generateRequests(getCurrentPlayer());
        return ModelResponse.ALLOW;
    }

    /**
     * @see AbstractGameState#buildDome(int, Coordinates)
     */
    @Override
    public ModelResponse buildDome(int worker, Coordinates destination) {
        Worker modelWorker = getWorkerById(worker);

        if (modelWorker == null) {
            return ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(modelWorker);

        if (turn.isEmpty()) {
            return ModelResponse.INVALID_PARAMS;
        }

        if (!getAvailableDomeBuilds(turn.get()).contains(destination)) {
            return ModelResponse.INVALID_PARAMS;
        }

        Cell cell = getBoard().getCell(destination);
        Player player = getCurrentPlayer();
        player.doBuildDome(turn.get(), cell);
        notifyMovements(player.getName(), turn.get());

        this.turn = turn.get();

        var event = new WorkerBuildDomeEvent(player.getName(), worker, destination);
        setReceivers(event);
        event.accept(getModelEventProvider());

        generateRequests(getCurrentPlayer());
        return ModelResponse.ALLOW;
    }

    /**
     * @see AbstractGameState#forceWorker(int, int, Coordinates)
     */
    @Override
    public ModelResponse forceWorker(int worker, int target, Coordinates destination) {
        Worker modelWorker = getWorkerById(worker);
        Worker modelTarget = getOtherWorkerById(target);

        if (modelWorker == null) {
            return ModelResponse.INVALID_PARAMS;
        }

        if (modelTarget == null) {
            return ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(modelWorker);

        if (turn.isEmpty()) {
            return ModelResponse.INVALID_PARAMS;
        }

        if (!getAvailableForces(turn.get(), modelTarget).contains(destination)) {
            return ModelResponse.INVALID_PARAMS;
        }

        Cell cell = getBoard().getCell(destination);
        Player player = getCurrentPlayer();
        player.doForce(turn.get(), modelTarget, cell);
        notifyMovements(player.getName(), turn.get());

        this.turn = turn.get();

        var event = new WorkerForceEvent(player.getName(), worker, target, destination);
        setReceivers(event);
        event.accept(getModelEventProvider());

        generateRequests(getCurrentPlayer());
        return ModelResponse.ALLOW;
    }

    /**
     * @see AbstractGameState#endTurn()
     */
    @Override
    public ModelResponse endTurn() {
        if (!checkCanEndTurn()) {
            return ModelResponse.INVALID_STATE;
        }

        Turn turn = this.turn;
        this.turn = null;

        if (turn == null) {
            // The player had to end the turn without doing anything
            doLose();
            return ModelResponse.ALLOW;
        }

        if (hasCompletedMandatoryInteractions(turn)) {
            playerIndex = (playerIndex + 1) % getPlayers().size();

            var event = new PlayerTurnStartEvent(getCurrentPlayer().getName());
            setReceivers(event);
            event.accept(getModelEventProvider());
            generateRequests(getCurrentPlayer());
            return ModelResponse.ALLOW;
        }

        // The player had to end the turn without being able to either move or build after moving
        doLose();
        return ModelResponse.ALLOW;
    }

    /**
     * @see AbstractGameState#getCurrentPlayer()
     */
    @Override
    public Player getCurrentPlayer() {
        return getPlayers().get(playerIndex);
    }

    /**
     * @see AbstractGameState#nextState()
     */
    @Override
    public AbstractGameState nextState() {
        if (isDone()) {
            return new EndGame(getModelEventProvider(), getBoard(), getPlayers().get(0).getName(), getAllPlayers());
        }

        return this;
    }

    /**
     * Get the available moves for a worker
     * @param turn The Turn
     */
    private List<Coordinates> getAvailableMoves(Turn turn) {
        return getAvailable((cell) -> getCurrentPlayer().checkCanMove(turn, cell));
    }

    /**
     * Get the available builds for a worker
     * @param turn The Turn
     */
    private List<Coordinates> getAvailableBlockBuilds(Turn turn) {
        return getAvailable((cell) -> getCurrentPlayer().checkCanBuildBlock(turn, cell));
    }

    /**
     * Get the available dome builds for a worker
     * @param turn The Turn
     */
    private List<Coordinates> getAvailableDomeBuilds(Turn turn) {
        return getAvailable((cell) -> getCurrentPlayer().checkCanBuildDome(turn, cell));
    }

    /**
     * Get the available force moves for the worker targeting an opponent worker
     * @param turn The Turn
     * @param target The worker to be forced
     */
    private List<Coordinates> getAvailableForces(Turn turn, Worker target) {
        return getAvailable((cell) -> getCurrentPlayer().checkCanForce(turn, target, cell));
    }

    /**
     * Check if the current player can end the turn
     * @return true if the turn can be ended
     */
    private boolean checkCanEndTurn() {
        if (this.turn == null) {
            for (Worker worker : getCurrentPlayer().getWorkers()) {
                if (hasOptions(generateTurn(worker))) {
                    return false;
                }
            }

            return true;
        }

        if (hasCompletedMandatoryInteractions(this.turn)) {
            return true;
        }

        return !hasOptions(turn);
    }

    /**
     * Check if there is only on player remaining
     * @return true if there is only one player
     */
    private boolean isDone() {
        return getPlayers().size() == 1;
    }

    /**
     * Generates requests from a player
     * @param player The selected player
     */
    private void generateRequests(Player player) {
        if (turn != null) {
            generateRequests(player, turn);
            return;
        }

        for (Worker worker : player.getWorkers()) {
            generateRequests(player, generateTurn(worker));
        }
    }

    /**
     * Generates requests from a player and a turn
     * @param player The selected player
     * @param turn The current turn
     */
    private void generateRequests(Player player, Turn turn) {
        // Generate the request for block builds if available
        List<Coordinates> availableBlockBuilds = getAvailableBlockBuilds(turn);

        if (availableBlockBuilds.size() > 0) {
            new RequestWorkerBuildBlockEvent(player.getName(), turn.getWorker().getId(), availableBlockBuilds)
                    .accept(getModelEventProvider());
        }

        // Generate the request for dome builds if available
        List<Coordinates> availableDomeBuilds = getAvailableDomeBuilds(turn);

        if (availableDomeBuilds.size() > 0) {
            new RequestWorkerBuildDomeEvent(player.getName(), turn.getWorker().getId(), availableDomeBuilds)
                    .accept(getModelEventProvider());
        }

        // Generate the request for dome builds if available
        List<Coordinates> availableMoves = getAvailableMoves(turn);

        if (availableMoves.size() > 0) {
            new RequestWorkerMoveEvent(player.getName(), turn.getWorker().getId(), availableMoves)
                    .accept(getModelEventProvider());
        }

        // Generate the request for forces if available
        List<Worker> otherWorkers = new ArrayList<>();
        for (Player other : getOpponents(player)) {
            otherWorkers.addAll(other.getWorkers());
        }

        Map<Integer, List<Coordinates>> availableTargetDestinations = new HashMap<>();
        for (Worker other : otherWorkers) {
            List<Coordinates> availableForces = getAvailableForces(turn, other);

            if (availableForces.size() > 0) {
                availableTargetDestinations.put(other.getId(), availableForces);
            }
        }

        if (availableTargetDestinations.size() > 0) {
            new RequestWorkerForceEvent(player.getName(), turn.getWorker().getId(), availableTargetDestinations)
                    .accept(getModelEventProvider());
        }

        // Generate the request for end turn if available
        // This request also signals the client that no other request will be sent
        boolean canBeEnded = checkCanEndTurn();
        new RequestPlayerEndTurnEvent(player.getName(), canBeEnded)
                .accept(getModelEventProvider());
    }

    /**
     * Obtains the avaible coordinates
     * @param filter The predicate
     */
    private List<Coordinates> getAvailable(Predicate<Cell> filter) {
        List<Cell> cells = new ArrayList<>();

        getBoard().getCells().forEach(cell -> {
            if (filter.test(cell)) {
                cells.add(cell);
            }
        });

        return toCoordinatesList(cells);
    }

    /**
     * Get or generates a turn (if not present)
     * @param worker The selected worker
     */
    private Optional<Turn> getOrGenerateTurn(Worker worker) {
        if (turn != null) {
            if (!turn.getWorker().equals(worker)) {
                // Can't use more than one worker per turn
                return Optional.empty();
            }

            return Optional.of(turn);
        }

        return Optional.of(generateTurn(worker));
    }

    /**
     * Generates a turn for aworker
     * @param worker The selected worker
     */
    private Turn generateTurn(Worker worker) {
        Map<Worker, Boolean> otherWorkers = new HashMap<>();

        for (Player player : getPlayers()) {
            for (Worker other : player.getWorkers()) {
                if (other.equals(worker)) {
                    continue;
                }

                otherWorkers.put(other, player.equals(getCurrentPlayer()));
            }
        }

        return new Turn(worker, otherWorkers, cell -> getBoard().getNeighborings(cell), cell -> getBoard().isPerimeterSpace(cell));
    }

    /**
     * Check if a player has options a in that turn
     * @param turn The turn
     * @return true if the player has options
     */
    private boolean hasOptions(Turn turn) {
        if (getAvailableBlockBuilds(turn).size() > 0) {
            return true;
        }

        if (getAvailableDomeBuilds(turn).size() > 0) {
            return true;
        }

        return getAvailableMoves(turn).size() > 0;
    }

    /**
     * Check if a player has completed his mandatory interactions
     * @param turn The turn
     * @return true if the player has completed the interactions
     */
    private boolean hasCompletedMandatoryInteractions(Turn turn) {
        boolean moved = false;
        boolean built = false;

        for (Turn.Action action : turn.getStandardActions()) {
            if (action.getType() == Turn.ActionType.MOVEMENT) {
                moved = true;
            } else if (action.getType().isBuild() && moved) {
                built = true;
            }
        }

        return built;
    }
    /**
     * Removes the losing player and notifies him and the others player of his defeat
     *
     * The player abilities will be reset to remove possible "opponents" effects added by the player that has lost
     */
    private void doLose() {
        Player player = getCurrentPlayer();
        removePlayer(player);

        for (Player other : getPlayers()) {
            other.resetAbilities();
        }

        for (Player other : getPlayers()) {
            Optional<God> god = other.getGod();

            if (god.isEmpty()) {
                continue;
            }

            for (Player enemy : getPlayers()) {
                if (enemy.equals(other)) {
                    continue;
                }

                enemy.applyOpponentGod(god.get(), other);
            }
        }

        playerIndex = playerIndex % getPlayers().size();

        var event = new PlayerLoseEvent(player.getName());
        setReceivers(event);
        event.accept(getModelEventProvider());

        if (!isDone()) {
            var startEvent = new PlayerTurnStartEvent(getCurrentPlayer().getName());
            setReceivers(startEvent);
            startEvent.accept(getModelEventProvider());

            generateRequests(getCurrentPlayer());
        }
    }

    /**
     * Notifies the movement of the player's workers ina turn
     * @param player The selected player
     * @param turn The current turn
     */
    private void notifyMovements(String player, Turn turn) {
        List<Worker> movedWorkers = turn.getMovedWorkers();

        for (Worker worker : movedWorkers) {
            var event = new WorkerMoveEvent(player, worker.getId(), toCoordinates(worker.getCell()));
            setReceivers(event);
            event.accept(getModelEventProvider());
        }

        turn.clearMovedWorkers();
    }

    /**
     * Converts a cells in his corresponding coordinates
     * @param cell The selected cell
     * @return The coordinates
     */
    private Coordinates toCoordinates(Cell cell) {
        return new Coordinates(cell.getX(), cell.getY());
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
     * Retrieves a worker instance from its id (the worker is of the current player)
     * @param id The worker's id
     * @return The worker
     */
    private Worker getWorkerById(int id) {
        for (Worker worker : getCurrentPlayer().getWorkers()) {
            if (worker.getId() == id) {
                return worker;
            }
        }

        return null;
    }

    /**
     * Retrieves a worker instance from its id (the worker is not of the current player)
     * @param id The worker's id
     * @return The worker
     */
    private Worker getOtherWorkerById(int id) {
        for (Player player : getPlayers()) {
            for (Worker worker : player.getWorkers()) {
                if (worker.getId() == id) {
                    return worker;
                }
            }
        }

        return null;
    }

}
