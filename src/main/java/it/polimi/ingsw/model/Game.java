package it.polimi.ingsw.model;

import it.polimi.ingsw.model.gamestates.AbstractGameState;
import it.polimi.ingsw.model.gamestates.PreGodsGame;
import it.polimi.ingsw.model.gamestates.PreWorkersGame;

import java.util.*;

public class Game {

    public static final int BOARD_ROWS = 5;
    public static final int BOARD_COLUMNS = 5;
    public static final int MAX_WORKERS = 2;

    /**
     * The current state of the game, implementing the available interactions
     */
    private AbstractGameState currentState;

    /**
     * Instantiates the Game
     * @param players The Players
     * @param simpleGame True for a simple game, skipping god selection
     */
    public Game(List<Player> players, boolean simpleGame) {
        /*
         * TODO: Implement configuration loading
         * Needs to load: Board configuration, deck
         */

        Board board = new Board(BOARD_ROWS, BOARD_COLUMNS);
        Deck deck = new Deck(List.of());

        if (simpleGame) {
            currentState = new PreWorkersGame(board, players, MAX_WORKERS);
        } else {
            currentState = new PreGodsGame(board, players, MAX_WORKERS, deck.getGods());
        }
    }

    /**
     * Obtain the game Board
     * @return The Board
     *
     * <strong>This method has no side effect</strong>
     */
    public Board getBoard() {
        return currentState.getBoard();
    }

    /**
     * Obtain the list of every Player
     * @return The list of players
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Player> getPlayers() {
        return currentState.getPlayers();
    }

    /**
     * Get the list of the other players
     * @param player The player
     * @return The list of opponent players
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Player> getOpponents(Player player) {
        return currentState.getOpponents(player);
    }

    /**
     * Obtain the current player that is able to interact with the game
     * Calling this method repeatedly should not result in a different player unless other methods got called
     * @return The Player
     */
    public Player getCurrentPlayer() {
        return currentState.getCurrentPlayer();
    }

    /**
     * Get the list of available gods
     * If the selection has not been made the list will contain every god configured
     * Otherwise, the list will contain only the remaining gods that can be picked by the current player
     *
     * @return The List of gods
     *
     * <strong>This method has no side effect</strong>
     */
    public List<God> getAvailableGods() {
        return currentState.getAvailableGods();
    }

    /**
     * Get the number of cards to be selected
     *
     * <strong>This method has no side effect</strong>
     */
    public int getSelectGodsCount() {
        return currentState.getSelectGodsCount();
    }

    /**
     * Check if the gods provided can be selected
     * @param gods The list of the chosen god cards
     * @return true if the selection is valid
     *
     * <strong>This method has no side effect</strong>
     */
    public boolean checkCanSelectGods(List<God> gods) {
        return currentState.checkCanSelectGods(gods);
    }

    /**
     * Select the god cards to be used in the current game
     * @param gods The list of the chosen god cards
     */
    public void selectGods(List<God> gods) {
        currentState.selectGods(gods);
        updateState();
    }

    /**
     * Select the god card, between the available gods, to be used by the current player
     * @param god The god card, must be still available
     */
    public void chooseGod(God god) {
        currentState.chooseGod(god);
        updateState();
    }

    /**
     * The list of available cells where a new Worker can be placed
     * @return The list of cells
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Cell> getAvailableCells() {
        return currentState.getAvailableCells();
    }

    /**
     * Spawns the worker, adding it to the current Player
     * @param worker The Worker
     */
    public void spawnWorker(Worker worker) {
        currentState.spawnWorker(worker);
        updateState();
    }

    /**
     * Get the available moves for a worker
     * @param worker The worker to move
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Cell> getAvailableMoves(Worker worker) {
        return currentState.getAvailableMoves(worker);
    }

    /**
     * Move a worker to another cell
     * @param worker The worker to move
     * @param destination The destination of the worker
     */
    public void moveWorker(Worker worker, Cell destination) {
        currentState.moveWorker(worker, destination);
        updateState();
    }

    /**
     * Get the available builds for a worker
     * @param worker The worker building the block
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Cell> getAvailableBlockBuilds(Worker worker) {
        return currentState.getAvailableBlockBuilds(worker);
    }

    /**
     * Build a block in the destination cell
     * @param worker The worker who is building
     * @param destination The destination cell
     */
    public void buildBlock(Worker worker, Cell destination) {
        currentState.buildBlock(worker, destination);
        updateState();
    }

    /**
     * Get the available dome builds for a worker
     * @param worker The worker building the block
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Cell> getAvailableDomeBuilds(Worker worker) {
        return currentState.getAvailableDomeBuilds(worker);
    }

    /**
     * Build a dome in the destination cell
     * @param worker The worker who is building
     * @param destination The destination cell
     */
    public void buildDome(Worker worker, Cell destination) {
        currentState.buildDome(worker, destination);
        updateState();
    }

    /**
     * Get the available force moves for the worker targeting an opponent worker
     * @param worker The worker to use
     * @param target The worker to be forced
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Cell> getAvailableForces(Worker worker, Worker target) {
        return currentState.getAvailableForces(worker, target);
    }

    /**
     * Force the target to another cell
     * @param worker The worker to use
     * @param target The worker to be forced
     * @param destination The destination of the target
     */
    public void forceWorker(Worker worker, Worker target, Cell destination) {
        currentState.forceWorker(worker, target, destination);
        updateState();
    }

    /**
     * Check if the current player can end the turn
     * @return true if the turn can be ended
     *
     * <strong>This method has no side effect</strong>
     */
    public boolean checkCanEndTurn() {
        return currentState.checkCanEndTurn();
    }

    /**
     * Ends the current turn
     */
    public void endTurn() {
        currentState.endTurn();
        updateState();
    }

    private void updateState() {
        currentState = currentState.nextState();
    }

}
