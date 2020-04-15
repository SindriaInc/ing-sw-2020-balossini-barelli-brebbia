package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    }

    @Override
    public List<Cell> getAvailableMoves(Worker worker) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanMove(turn, cell));
    }

    @Override
    public void moveWorker(Worker worker, Cell destination) {
        if (!getAvailableMoves(worker).contains(destination)) {
            throw new IllegalArgumentException("Invalid destination");
        }

        Turn turn = getOrGenerateTurn(worker);
        Player player = getCurrentPlayer();

        player.doMove(turn, destination);

        this.turn = turn;
    }

    @Override
    public List<Cell> getAvailableBlockBuilds(Worker worker) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanBuildBlock(turn, cell));
    }

    @Override
    public void buildBlock(Worker worker, Cell destination) {
        if (!getAvailableBlockBuilds(worker).contains(destination)) {
            throw new IllegalArgumentException("Invalid destination");
        }

        Turn turn = getOrGenerateTurn(worker);
        Player player = getCurrentPlayer();

        player.doBuildBlock(turn, destination);

        this.turn = turn;
    }

    @Override
    public List<Cell> getAvailableDomeBuilds(Worker worker) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanBuildDome(turn, cell));
    }

    @Override
    public void buildDome(Worker worker, Cell destination) {
        if (!getAvailableDomeBuilds(worker).contains(destination)) {
            throw new IllegalArgumentException("Invalid destination");
        }

        Turn turn = getOrGenerateTurn(worker);
        Player player = getCurrentPlayer();

        player.doBuildDome(turn, destination);

        this.turn = turn;
    }

    @Override
    public List<Cell> getAvailableForces(Worker worker, Worker target) {
        return getAvailable(worker, (turn, cell) -> getCurrentPlayer().checkCanForce(turn, target, cell));
    }

    @Override
    public void forceWorker(Worker worker, Worker target, Cell destination) {
        if (!getAvailableForces(worker, target).contains(destination)) {
            throw new IllegalArgumentException("Invalid destination");
        }

        Turn turn = getOrGenerateTurn(worker);
        Player player = getCurrentPlayer();

        player.doForce(turn, target, destination);

        this.turn = turn;
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

        return !hasOptions(turn.getWorker());
    }

    @Override
    public void endTurn() {
        if (!checkCanEndTurn()) {
            throw new IllegalStateException("Unable to end the turn now");
        }

        Turn turn = this.turn;
        this.turn = null;

        if (turn == null) {
            // The player had to end the turn without doing anything
            doLose();
            return;
        }

        boolean moved = false;
        boolean built = false;

        for (Turn.Action action : turn.getActions()) {
            if (action.getType() == Turn.ActionType.MOVEMENT) {
                moved = true;
            } else if (action.getType().isBuild() && moved) {
                built = true;
            }
        }

        if (built) {
            playerIndex = (playerIndex + 1) % getPlayers().size();
            return;
        }

        // The player had to end the turn without being able to either move or build after moving
        doLose();
    }

    @Override
    public AbstractGameState nextState() {
        if (getPlayers().size() == 1) {
            return new EndGame(getBoard(), getPlayers());
        }

        return this;
    }

    @Override
    public Player getCurrentPlayer() {
        return getPlayers().get(playerIndex);
    }

    private List<Cell> getAvailable(Worker worker, BiPredicate<Turn, Cell> filter) {
        if (!getCurrentPlayer().getWorkers().contains(worker)) {
            throw new IllegalArgumentException("Can't use another player's workers");
        }

        Turn turn = getOrGenerateTurn(worker);
        List<Cell> cells = new ArrayList<>();

        getBoard().getCells().parallelStream().forEach(cell -> {
            if (filter.test(turn, cell)) {
                cells.add(cell);
            }
        });

        return cells;
    }

    private Turn getOrGenerateTurn(Worker worker) {
        if (turn != null) {
            if (!turn.getWorker().equals(worker)) {
                throw new IllegalArgumentException("Can't use more than one worker per turn");
            }

            return turn;
        }

        return generateTurn(worker);
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

    private void doLose() {
        Player player = getCurrentPlayer();
        removePlayer(player);

        playerIndex = playerIndex % getPlayers().size();
    }

}
