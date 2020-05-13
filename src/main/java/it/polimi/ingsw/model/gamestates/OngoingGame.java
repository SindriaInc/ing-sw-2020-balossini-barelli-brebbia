package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.request.*;
import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.function.Predicate;

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

    public OngoingGame(ModelEventProvider provider, Board board, List<Player> players) {
        super(provider, board, players);

        Player player = getCurrentPlayer();
        var event = new PlayerTurnStartEvent(player.getName());
        setReceivers(event);
        getModelEventProvider().getPlayerTurnStartEventObservable().notifyObservers(event);
        generateRequests(player);
    }

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

        this.turn = turn.get();

        var event = new WorkerMoveEvent(player.getName(), worker, destination);
        setReceivers(event);
        getModelEventProvider().getWorkerMoveEventObservable().notifyObservers(event);

        generateRequests(getCurrentPlayer());
        return ModelResponse.ALLOW;
    }

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

        this.turn = turn.get();

        var event = new WorkerBuildBlockEvent(player.getName(), worker, destination);
        setReceivers(event);
        getModelEventProvider().getWorkerBuildBlockEventObservable().notifyObservers(event);

        generateRequests(getCurrentPlayer());
        return ModelResponse.ALLOW;
    }

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

        this.turn = turn.get();

        var event = new WorkerBuildDomeEvent(player.getName(), worker, destination);
        setReceivers(event);
        getModelEventProvider().getWorkerBuildDomeEventObservable().notifyObservers(event);

        generateRequests(getCurrentPlayer());
        return ModelResponse.ALLOW;
    }

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

        this.turn = turn.get();

        var event = new WorkerForceEvent(player.getName(), worker, target, destination);
        setReceivers(event);
        getModelEventProvider().getWorkerForceEventObservable().notifyObservers(event);

        generateRequests(getCurrentPlayer());
        return ModelResponse.ALLOW;
    }

    @Override
    public ModelResponse endTurn() {
        if (!checkCanEndTurn()) {
            return ModelResponse.INVALID_STATE;
        }

        Player endingPlayer = getCurrentPlayer();
        Turn turn = this.turn;
        this.turn = null;

        if (turn == null) {
            // The player had to end the turn without doing anything
            doLose();
            return ModelResponse.ALLOW;
        }

        if (hasCompletedMandatoryInteractions(turn)) {
            if (!endingPlayer.checkHasWon(turn)) {
                playerIndex = (playerIndex + 1) % getPlayers().size();

                var event = new PlayerTurnStartEvent(getCurrentPlayer().getName());
                setReceivers(event);
                getModelEventProvider().getPlayerTurnStartEventObservable().notifyObservers(event);
                generateRequests(getCurrentPlayer());
                return ModelResponse.ALLOW;
            }

            // The current player has won, remove every opponent
            getOpponents(endingPlayer).forEach(super::removePlayer);
            return ModelResponse.ALLOW;
        }

        // The player had to end the turn without being able to either move or build after moving
        doLose();
        return ModelResponse.ALLOW;
    }

    @Override
    public Player getCurrentPlayer() {
        return getPlayers().get(playerIndex);
    }

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

    private boolean isDone() {
        return getPlayers().size() == 1;
    }

    private void generateRequests(Player player) {
        if (turn != null) {
            generateRequests(player, turn);
            return;
        }

        for (Worker worker : player.getWorkers()) {
            generateRequests(player, generateTurn(worker));
        }
    }

    private void generateRequests(Player player, Turn turn) {
        // Generate the request for block builds if available
        List<Coordinates> availableBlockBuilds = getAvailableBlockBuilds(turn);

        if (availableBlockBuilds.size() > 0) {
            getModelEventProvider().getRequestWorkerBuildBlockEventObservable().notifyObservers(
                    new RequestWorkerBuildBlockEvent(player.getName(), turn.getWorker().getId(), availableBlockBuilds)
            );
        }

        // Generate the request for dome builds if available
        List<Coordinates> availableDomeBuilds = getAvailableDomeBuilds(turn);

        if (availableDomeBuilds.size() > 0) {
            getModelEventProvider().getRequestWorkerBuildDomeEventObservable().notifyObservers(
                    new RequestWorkerBuildDomeEvent(player.getName(), turn.getWorker().getId(), availableDomeBuilds)
            );
        }

        // Generate the request for dome builds if available
        List<Coordinates> availableMoves = getAvailableMoves(turn);

        if (availableMoves.size() > 0) {
            getModelEventProvider().getRequestWorkerMoveEventObservable().notifyObservers(
                    new RequestWorkerMoveEvent(player.getName(), turn.getWorker().getId(), availableMoves)
            );
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
            getModelEventProvider().getRequestWorkerForceEventObservable().notifyObservers(
                    new RequestWorkerForceEvent(player.getName(), turn.getWorker().getId(), availableTargetDestinations)
            );
        }

        // Generate the request for end turn if available
        // This request also signals the client that no other request will be sent
        boolean canBeEnded = checkCanEndTurn();
        getModelEventProvider().getRequestPlayerEndTurnEventObservable().notifyObservers(
                new RequestPlayerEndTurnEvent(player.getName(), canBeEnded)
        );
    }

    private List<Coordinates> getAvailable(Predicate<Cell> filter) {
        List<Cell> cells = new ArrayList<>();

        getBoard().getCells().parallelStream().forEach(cell -> {
            if (filter.test(cell)) {
                cells.add(cell);
            }
        });

        return toCoordinatesList(cells);
    }

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

    private boolean hasOptions(Turn turn) {
        if (getAvailableBlockBuilds(turn).size() > 0) {
            return true;
        }

        if (getAvailableDomeBuilds(turn).size() > 0) {
            return true;
        }

        return getAvailableMoves(turn).size() > 0;
    }

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

    private void doLose() {
        Player player = getCurrentPlayer();
        removePlayer(player);

        playerIndex = playerIndex % getPlayers().size();

        var event = new PlayerLoseEvent(player.getName());
        setReceivers(event);
        getModelEventProvider().getPlayerLoseEventObservable().notifyObservers(event);

        if (!isDone()) {
            generateRequests(getCurrentPlayer());
        }
    }

    private List<Coordinates> toCoordinatesList(List<Cell> cells) {
        List<Coordinates> coordinates = new ArrayList<>();

        for (Cell cell : cells) {
            coordinates.add(new Coordinates(cell.getX(), cell.getY()));
        }

        return coordinates;
    }

    private Worker getWorkerById(int id) {
        for (Worker worker : getCurrentPlayer().getWorkers()) {
            if (worker.getId() == id) {
                return worker;
            }
        }

        return null;
    }

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
