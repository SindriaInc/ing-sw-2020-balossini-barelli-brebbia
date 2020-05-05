package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.request.*;
import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.function.BiPredicate;

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
        getModelEventProvider().getPlayerTurnStartEventObservable().notifyObservers(
                new PlayerTurnStartEvent(player.getName())
        );
        generateRequests(player);
    }

    /**
     * Get the available moves for a worker
     * @param worker The worker to move
     */
    private List<Coordinates> getAvailableMoves(Worker worker) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanMove(turn, cell));
    }

    @Override
    public Game.ModelResponse moveWorker(int worker, Coordinates destination) {
        Worker modelWorker = getWorkerById(worker);

        if (modelWorker == null) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(modelWorker);

        if (turn.isEmpty()) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        if (!getAvailableMoves(modelWorker).contains(destination)) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Cell cell = getBoard().getCell(destination);
        Player player = getCurrentPlayer();
        player.doMove(turn.get(), cell);

        this.turn = turn.get();

        getModelEventProvider().getWorkerMoveEventObservable().notifyObservers(
                new WorkerMoveEvent(player.getName(), worker, destination)
        );
        return Game.ModelResponse.ALLOW;
    }

    /**
     * Get the available builds for a worker
     * @param worker The worker building the block
     */
    private List<Coordinates> getAvailableBlockBuilds(Worker worker) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanBuildBlock(turn, cell));
    }

    @Override
    public Game.ModelResponse buildBlock(int worker, Coordinates destination) {
        Worker modelWorker = getWorkerById(worker);

        if (modelWorker == null) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(modelWorker);

        if (turn.isEmpty()) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        if (!getAvailableBlockBuilds(modelWorker).contains(destination)) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Cell cell = getBoard().getCell(destination);
        Player player = getCurrentPlayer();
        player.doBuildBlock(turn.get(), cell);

        this.turn = turn.get();

        getModelEventProvider().getWorkerBuildBlockEventObservable().notifyObservers(
                new WorkerBuildBlockEvent(player.getName(), worker, destination)
        );
        return Game.ModelResponse.ALLOW;
    }

    /**
     * Get the available dome builds for a worker
     * @param worker The worker building the block
     */
    private List<Coordinates> getAvailableDomeBuilds(Worker worker) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanBuildDome(turn, cell));
    }

    @Override
    public Game.ModelResponse buildDome(int worker, Coordinates destination) {
        Worker modelWorker = getWorkerById(worker);

        if (modelWorker == null) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(modelWorker);

        if (turn.isEmpty()) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        if (!getAvailableDomeBuilds(modelWorker).contains(destination)) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Cell cell = getBoard().getCell(destination);
        Player player = getCurrentPlayer();
        player.doBuildDome(turn.get(), cell);

        this.turn = turn.get();

        getModelEventProvider().getWorkerBuildDomeEventObservable().notifyObservers(
                new WorkerBuildDomeEvent(player.getName(), worker, destination)
        );
        return Game.ModelResponse.ALLOW;
    }

    /**
     * Get the available force moves for the worker targeting an opponent worker
     * @param worker The worker to use
     * @param target The worker to be forced
     */
    private List<Coordinates> getAvailableForces(Worker worker, Worker target) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanForce(turn, target, cell));
    }

    @Override
    public Game.ModelResponse forceWorker(int worker, int target, Coordinates destination) {
        Worker modelWorker = getWorkerById(worker);
        Worker modelTarget = getWorkerById(target);

        if (modelWorker == null) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(modelWorker);

        if (turn.isEmpty()) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        if (!getAvailableForces(modelWorker, modelTarget).contains(destination)) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Cell cell = getBoard().getCell(destination);
        Player player = getCurrentPlayer();
        player.doForce(turn.get(), modelTarget, cell);

        this.turn = turn.get();

        getModelEventProvider().getWorkerForceEventObservable().notifyObservers(
                new WorkerForceEvent(player.getName(), worker, target, destination)
        );
        return Game.ModelResponse.ALLOW;
    }

    /**
     * Check if the current player can end the turn
     * @return true if the turn can be ended
     */
    private boolean checkCanEndTurn() {
        if (this.turn == null) {
            for (Worker worker : getCurrentPlayer().getWorkers()) {
                if (hasOptions(worker)) {
                    return false;
                }
            }

            return true;
        }

        if (hasCompletedMandatoryInteractions(this.turn)) {
            return true;
        }

        return !hasOptions(turn.getWorker());
    }

    @Override
    public Game.ModelResponse endTurn() {
        if (!checkCanEndTurn()) {
            return Game.ModelResponse.INVALID_STATE;
        }

        Player endingPlayer = getCurrentPlayer();
        Turn turn = this.turn;
        this.turn = null;

        if (turn == null) {
            // The player had to end the turn without doing anything
            doLose();
            getModelEventProvider().getPlayerLoseEventObservable().notifyObservers(
                    new PlayerLoseEvent(endingPlayer.getName())
            );
            return Game.ModelResponse.ALLOW;
        }

        if (hasCompletedMandatoryInteractions(turn)) {
            if (!endingPlayer.checkHasWon(turn)) {
                playerIndex = (playerIndex + 1) % getPlayers().size();

                getModelEventProvider().getPlayerTurnStartEventObservable().notifyObservers(
                        new PlayerTurnStartEvent(getCurrentPlayer().getName())
                );
                return Game.ModelResponse.ALLOW;
            }

            // The current player has won, remove every opponent
            getOpponents(endingPlayer).forEach(super::removePlayer);
            return Game.ModelResponse.ALLOW;
        }

        // The player had to end the turn without being able to either move or build after moving
        doLose();
        getModelEventProvider().getPlayerLoseEventObservable().notifyObservers(
                new PlayerLoseEvent(endingPlayer.getName())
        );
        return Game.ModelResponse.ALLOW;
    }

    @Override
    public Player getCurrentPlayer() {
        return getPlayers().get(playerIndex);
    }

    @Override
    public AbstractGameState nextState() {
        if (getPlayers().size() == 1) {
            return new EndGame(getModelEventProvider(), getBoard(), getPlayers().get(0));
        }

        generateRequests(getCurrentPlayer());
        return this;
    }

    private void generateRequests(Player player) {
        if (turn != null) {
            generateRequests(player, turn.getWorker());
            return;
        }

        for (Worker worker : player.getWorkers()) {
            generateRequests(player, worker);
        }
    }

    private void generateRequests(Player player, Worker worker) {
        // Generate the request for block builds if available
        List<Coordinates> availableBlockBuilds = getAvailableBlockBuilds(worker);

        if (availableBlockBuilds.size() > 0) {
            getModelEventProvider().getRequestWorkerBuildBlockEventObservable().notifyObservers(
                    new RequestWorkerBuildBlockEvent(player.getName(), worker.getId(), availableBlockBuilds)
            );
        }

        // Generate the request for dome builds if available
        List<Coordinates> availableDomeBuilds = getAvailableDomeBuilds(worker);

        if (availableDomeBuilds.size() > 0) {
            getModelEventProvider().getRequestWorkerBuildDomeEventObservable().notifyObservers(
                    new RequestWorkerBuildDomeEvent(player.getName(), worker.getId(), availableDomeBuilds)
            );
        }

        // Generate the request for dome builds if available
        List<Coordinates> availableMoves = getAvailableMoves(worker);

        if (availableMoves.size() > 0) {
            getModelEventProvider().getRequestWorkerMoveEventObservable().notifyObservers(
                    new RequestWorkerMoveEvent(player.getName(), worker.getId(), availableMoves)
            );
        }

        // Generate the request for forces if available
        List<Worker> otherWorkers = new ArrayList<>();
        for (Player other : getOpponents(player)) {
            otherWorkers.addAll(other.getWorkers());
        }

        Map<Integer, List<Coordinates>> availableTargetDestinations = new HashMap<>();
        for (Worker other : otherWorkers) {
            List<Coordinates> availableForces = getAvailableForces(worker, other);

            if (availableForces.size() > 0) {
                availableTargetDestinations.put(other.getId(), availableForces);
            }
        }

        if (availableTargetDestinations.size() > 0) {
            getModelEventProvider().getRequestWorkerForceEventObservable().notifyObservers(
                    new RequestWorkerForceEvent(player.getName(), worker.getId(), availableTargetDestinations)
            );
        }

        // Generate the request for end turn if available
        // This request also signals the client that no other request will be sent
        boolean canBeEnded = checkCanEndTurn();
        getModelEventProvider().getRequestPlayerEndTurnEventObservable().notifyObservers(
                new RequestPlayerEndTurnEvent(player.getName(), canBeEnded)
        );
    }

    private List<Coordinates> getAvailable(Worker worker, BiPredicate<Turn, Cell> filter) {
        Optional<Turn> turn = getOrGenerateTurn(worker);

        if (turn.isEmpty()) {
            // Can't use more than one worker per turn
            return List.of();
        }

        if (!getCurrentPlayer().getWorkers().contains(worker)) {
            // Can't use another player's workers
            return List.of();
        }

        List<Cell> cells = new ArrayList<>();

        getBoard().getCells().parallelStream().forEach(cell -> {
            if (filter.test(turn.get(), cell)) {
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

    private boolean hasOptions(Worker worker) {
        if (getAvailableBlockBuilds(worker).size() > 0) {
            return true;
        }

        if (getAvailableDomeBuilds(worker).size() > 0) {
            return true;
        }

        return getAvailableMoves(worker).size() > 0;
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

}
