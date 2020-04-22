package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.*;

import java.util.*;

public class PreWorkersGame extends AbstractGameState {

    /**
     * The current player index (refers to getPlayers())
     */
    private int playerIndex;

    /**
     * The maximum number of workers per player
     */
    private final int maxWorkers;

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
    }

    @Override
    public List<Cell> getAvailableCells() {
        List<Cell> cells = getBoard().getCells();

        for (Player player : getPlayers()) {
            for (Worker other : player.getWorkers()) {
                cells.remove(other.getCell());
            }
        }

        return cells;
    }

    @Override
    public void spawnWorker(Worker worker) {
        if (!getAvailableCells().contains(worker.getCell())) {
            throw new IllegalArgumentException("Invalid cell selected");
        }

        Player player = getCurrentPlayer();

        player.addWorker(worker);
    }

    @Override
    public Player getCurrentPlayer() {
        Player currentPlayer = getPlayers().get(playerIndex);

        if (currentPlayer.getWorkers().size() < maxWorkers) {
            return currentPlayer;
        }

        playerIndex++;
        return getPlayers().get(playerIndex);
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

    @Override
    public boolean isEnded() {
        return false;
    }

}
