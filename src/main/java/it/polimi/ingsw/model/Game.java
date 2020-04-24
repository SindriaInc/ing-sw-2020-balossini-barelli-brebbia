package it.polimi.ingsw.model;

import it.polimi.ingsw.common.Observer;
import it.polimi.ingsw.common.events.*;
import it.polimi.ingsw.model.gamestates.AbstractGameState;
import it.polimi.ingsw.model.gamestates.PreGodsGame;
import it.polimi.ingsw.model.gamestates.PreWorkersGame;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public enum ModelResponse {

        ALLOW,
        INVALID_PARAMS,
        INVALID_STATE

    }

    public static final int BOARD_ROWS = 5;
    public static final int BOARD_COLUMNS = 5;
    public static final int MAX_WORKERS = 2;

    /**
     * The current state of the game, implementing the available interactions
     */
    private AbstractGameState currentState;

    /**
     * Instantiates the Game
     * @param players The list of players
     * @param deck The deck, containing the list of Gods
     * @param simpleGame True for a simple game, skipping god selection
     */
    public Game(List<Player> players, Deck deck, boolean simpleGame) {
        Board board = new Board(BOARD_ROWS, BOARD_COLUMNS);

        if (simpleGame) {
            currentState = new PreWorkersGame(board, players, MAX_WORKERS);
        } else {
            currentState = new PreGodsGame(board, players, MAX_WORKERS, deck.getGods());
        }
    }

    /**
     * Register the observer for ChallengerSelectGodsEvent in the related observable
     * @param observer The Observer
     */
    public void registerChallengerSelectGodsEventObserver(Observer<ChallengerSelectGodsEvent> observer) {
        currentState.registerChallengerSelectGodsEventObserver(observer);
    }

    /**
     * Register the observer for PlayerChooseGodEvent in the related observable
     * @param observer The Observer
     */
    public void registerPlayerChooseGodEventObserver(Observer<PlayerChooseGodEvent> observer) {
        currentState.registerPlayerChooseGodEventObserver(observer);
    }

    /**
     * Register the observer for PlayerLoseEvent in the related observable
     * @param observer The Observer
     */
    public void registerPlayerLoseEventObserver(Observer<PlayerLoseEvent> observer) {
        currentState.registerPlayerLoseEventObserver(observer);
    }

    /**
     * Register the observer for PlayerTurnStartEvent in the related observable
     * @param observer The Observer
     */
    public void registerPlayerTurnStartEventObserver(Observer<PlayerTurnStartEvent> observer) {
        currentState.registerPlayerTurnStartEventObserver(observer);
    }

    /**
     * Register the observer for PlayerWinListener in the related observable
     * @param observer The Observer
     */
    public void registerPlayerWinListenerObserver(Observer<PlayerWinListener> observer) {
        currentState.registerPlayerWinListenerObserver(observer);
    }

    /**
     * Register the observer for WorkerBuildBlockEvent in the related observable
     * @param observer The Observer
     */
    public void registerWorkerBuildBlockEventObserver(Observer<WorkerBuildBlockEvent> observer) {
        currentState.registerWorkerBuildBlockEventObserver(observer);
    }

    /**
     * Register the observer for WorkerBuildDomeEvent in the related observable
     * @param observer The Observer
     */
    public void registerWorkerBuildDomeEventObserver(Observer<WorkerBuildDomeEvent> observer) {
        currentState.registerWorkerBuildDomeEventObserver(observer);
    }

    /**
     * Register the observer for WorkerForceEvent in the related observable
     * @param observer The Observer
     */
    public void registerWorkerForceEventObserver(Observer<WorkerForceEvent> observer) {
        currentState.registerWorkerForceEventObserver(observer);
    }

    /**
     * Register the observer for WorkerMoveEvent in the related observable
     * @param observer The Observer
     */
    public void registerWorkerMoveEventObserver(Observer<WorkerMoveEvent> observer) {
        currentState.registerWorkerMoveEventObserver(observer);
    }

    /**
     * Register the observer for WorkerSpawnEvent in the related observable
     * @param observer The Observer
     */
    public void registerWorkerSpawnEventObserver(Observer<WorkerSpawnEvent> observer) {
        currentState.registerWorkerSpawnEventObserver(observer);
    }

    /**
     * Obtain a copy of the game Board
     * @return The Board
     *
     * <strong>This method has no side effect</strong>
     */
    public Board getBoard() {
        return copyOfBoard(currentState.getBoard());
    }

    /**
     * Obtain the current player that is able to interact with the game
     * If Game#isEnded returns true, this method returns the winner
     * Calling this method repeatedly should not result in a different player unless other methods got called
     * @return The Player
     */
    public Player getCurrentPlayer() {
        return currentState.getCurrentPlayer();
    }

    /**
     * Check if the game has ended, meaning that one of the player has won
     * If this method returns true, Game#getCurrentPlayer returns the winner
     * @return true if there is a winner
     *
     * <strong>This method has no side effect</strong>
     */
    public boolean isEnded() {
        return currentState.isEnded();
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
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse selectGods(List<God> gods) {
        ModelResponse response = currentState.selectGods(gods);
        updateState();
        return response;
    }

    /**
     * Select the god card, between the available gods, to be used by the current player
     * @param god The god card, must be still available
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse chooseGod(God god) {
        ModelResponse response = currentState.chooseGod(god);
        updateState();
        return response;
    }

    /**
     * The list of available cells where a new Worker can be placed
     * @return The list of cells
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Cell> getAvailableCells() {
        return copyOfCells(currentState.getAvailableCells());
    }

    /**
     * Spawns the worker, adding it to the current Player
     * @param worker The Worker
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse spawnWorker(Worker worker) {
        ModelResponse response = currentState.spawnWorker(worker);
        updateState();
        return response;
    }

    /**
     * Get the available moves for a worker
     * @param worker The worker to move
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Cell> getAvailableMoves(Worker worker) {
        return copyOfCells(currentState.getAvailableMoves(worker));
    }

    /**
     * Move a worker to another cell
     * @param worker The worker to move
     * @param destination The destination of the worker
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse moveWorker(Worker worker, Cell destination) {
        ModelResponse response = currentState.moveWorker(worker, destination);
        updateState();
        return response;
    }

    /**
     * Get the available builds for a worker
     * @param worker The worker building the block
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Cell> getAvailableBlockBuilds(Worker worker) {
        return copyOfCells(currentState.getAvailableBlockBuilds(worker));
    }

    /**
     * Build a block in the destination cell
     * @param worker The worker who is building
     * @param destination The destination cell
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse buildBlock(Worker worker, Cell destination) {
        ModelResponse response = currentState.buildBlock(worker, destination);
        updateState();
        return response;
    }

    /**
     * Get the available dome builds for a worker
     * @param worker The worker building the block
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Cell> getAvailableDomeBuilds(Worker worker) {
        return copyOfCells(currentState.getAvailableDomeBuilds(worker));
    }

    /**
     * Build a dome in the destination cell
     * @param worker The worker who is building
     * @param destination The destination cell
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse buildDome(Worker worker, Cell destination) {
        ModelResponse response = currentState.buildDome(worker, destination);
        updateState();
        return response;
    }

    /**
     * Get the available force moves for the worker targeting an opponent worker
     * @param worker The worker to use
     * @param target The worker to be forced
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Cell> getAvailableForces(Worker worker, Worker target) {
        return copyOfCells(currentState.getAvailableForces(worker, target));
    }

    /**
     * Force the target to another cell
     * @param worker The worker to use
     * @param target The worker to be forced
     * @param destination The destination of the target
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse forceWorker(Worker worker, Worker target, Cell destination) {
        ModelResponse response = currentState.forceWorker(worker, target, destination);
        updateState();
        return response;
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
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse endTurn() {
        ModelResponse response = currentState.endTurn();
        updateState();
        return response;
    }

    /**
     * Check if the specified Player has lost
     * @param player The Player
     * @return true if the Player has lost
     *
     * <strong>This method has no side effect</strong>
     */
    public boolean checkHasLost(Player player) {
        return currentState.checkHasLost(player);
    }

    protected Board getOriginalBoard() {
        return currentState.getBoard();
    }

    private void updateState() {
        currentState = currentState.nextState();
    }

    private Board copyOfBoard(Board original) {
        Board copy = new Board(original.getRows(), original.getColumns());

        for (Cell cell : copy.getCells()) {
            applyCopy(cell, original.getCellFromCoords(cell.getX(), cell.getY()));
        }

        return copy;
    }

    private List<Cell> copyOfCells(List<Cell> originals) {
        List<Cell> copy = new ArrayList<>();

        for (Cell original : originals) {
            copy.add(copyOfCell(original));
        }

        return copy;
    }

    private Cell copyOfCell(Cell original) {
        Cell copy = new Cell(original.getX(), original.getY());
        applyCopy(copy, original);
        return copy;
    }

    private void applyCopy(Cell cell, Cell original) {
        cell.setLevel(original.getLevel());
        cell.setDoomed(original.isDoomed());
    }

}
