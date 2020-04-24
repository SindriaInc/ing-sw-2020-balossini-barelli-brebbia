package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.events.*;
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

    public OngoingGame(Board board, List<Player> players) {
        super(board, players);

        getPlayerTurnStartEventObservable().notifyObservers(new PlayerTurnStartEvent(getCurrentPlayer()));
    }

    @Override
    public List<Cell> getAvailableMoves(Worker worker) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanMove(turn, cell));
    }

    @Override
    public Game.ModelResponse moveWorker(Worker worker, Cell destination) {
        if (!getAvailableMoves(worker).contains(destination)) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(worker);

        if (turn.isEmpty()) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Player player = getCurrentPlayer();
        player.doMove(turn.get(), destination);

        this.turn = turn.get();

        getWorkerMoveEventObservable().notifyObservers(new WorkerMoveEvent(worker, destination));
        return Game.ModelResponse.ALLOW;
    }

    @Override
    public List<Cell> getAvailableBlockBuilds(Worker worker) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanBuildBlock(turn, cell));
    }

    @Override
    public Game.ModelResponse buildBlock(Worker worker, Cell destination) {
        if (!getAvailableBlockBuilds(worker).contains(destination)) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(worker);

        if (turn.isEmpty()) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Player player = getCurrentPlayer();
        player.doBuildBlock(turn.get(), destination);

        this.turn = turn.get();

        getWorkerBuildBlockEventObservable().notifyObservers(new WorkerBuildBlockEvent(worker, destination));
        return Game.ModelResponse.ALLOW;
    }

    @Override
    public List<Cell> getAvailableDomeBuilds(Worker worker) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanBuildDome(turn, cell));
    }

    @Override
    public Game.ModelResponse buildDome(Worker worker, Cell destination) {
        if (!getAvailableDomeBuilds(worker).contains(destination)) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(worker);

        if (turn.isEmpty()) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Player player = getCurrentPlayer();
        player.doBuildDome(turn.get(), destination);

        this.turn = turn.get();

        getWorkerBuildDomeEventObservable().notifyObservers(new WorkerBuildDomeEvent(worker, destination));
        return Game.ModelResponse.ALLOW;
    }

    @Override
    public List<Cell> getAvailableForces(Worker worker, Worker target) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanForce(turn, target, cell));
    }

    @Override
    public Game.ModelResponse forceWorker(Worker worker, Worker target, Cell destination) {
        if (!getAvailableForces(worker, target).contains(destination)) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Optional<Turn> turn = getOrGenerateTurn(worker);

        if (turn.isEmpty()) {
            return Game.ModelResponse.INVALID_PARAMS;
        }

        Player player = getCurrentPlayer();
        player.doForce(turn.get(), target, destination);

        this.turn = turn.get();

        getWorkerForceEventObservable().notifyObservers(new WorkerForceEvent(worker, target, destination));
        return Game.ModelResponse.ALLOW;
    }

    @Override
    public boolean checkCanEndTurn() {
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
            getPlayerLoseEventObservable().notifyObservers(new PlayerLoseEvent(endingPlayer));
            return Game.ModelResponse.ALLOW;
        }

        if (hasCompletedMandatoryInteractions(turn)) {
            if (!endingPlayer.checkHasWon(turn)) {
                playerIndex = (playerIndex + 1) % getPlayers().size();

                getPlayerTurnStartEventObservable().notifyObservers(new PlayerTurnStartEvent(getCurrentPlayer()));
                return Game.ModelResponse.ALLOW;
            }

            // The current player has won, remove every opponent
            getOpponents(endingPlayer).forEach(super::removePlayer);
            return Game.ModelResponse.ALLOW;
        }

        // The player had to end the turn without being able to either move or build after moving
        doLose();
        getPlayerLoseEventObservable().notifyObservers(new PlayerLoseEvent(endingPlayer));
        return Game.ModelResponse.ALLOW;
    }

    @Override
    public Player getCurrentPlayer() {
        return getPlayers().get(playerIndex);
    }

    @Override
    public AbstractGameState nextState() {
        if (getPlayers().size() == 1) {
            return new EndGame(getBoard(), getPlayers().get(0));
        }

        return this;
    }

    @Override
    public boolean isEnded() {
        return false;
    }

    private List<Cell> getAvailable(Worker worker, BiPredicate<Turn, Cell> filter) {
        if (!getCurrentPlayer().getWorkers().contains(worker)) {
            // Can't use another player's workers
            return null;
        }

        Optional<Turn> turn = getOrGenerateTurn(worker);

        if (turn.isEmpty()) {
            // Can't use more than one worker per turn
            return null;
        }

        List<Cell> cells = new ArrayList<>();

        getBoard().getCells().parallelStream().forEach(cell -> {
            if (filter.test(turn.get(), cell)) {
                cells.add(cell);
            }
        });

        return cells;
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

}
