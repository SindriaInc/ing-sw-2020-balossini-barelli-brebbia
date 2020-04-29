package it.polimi.ingsw.model;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.ModelEventProvider;
import it.polimi.ingsw.common.Observer;
import it.polimi.ingsw.common.events.*;
import it.polimi.ingsw.common.events.requests.*;
import it.polimi.ingsw.model.gamestates.AbstractGameState;
import it.polimi.ingsw.model.gamestates.PreGodsGame;
import it.polimi.ingsw.model.gamestates.PreWorkersGame;

import java.util.List;

public class Game implements ModelEventProvider {

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
     * @see ModelEventProvider#registerRequestChallengerSelectGodsEventObserver(Observer)
     */
    @Override
    public void registerRequestChallengerSelectGodsEventObserver(Observer<RequestChallengerSelectGodsEvent> observer) {
        currentState.registerRequestChallengerSelectGodsEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerRequestPlayerChooseGodEventObserver(Observer)
     */
    @Override
    public void registerRequestPlayerChooseGodEventObserver(Observer<RequestPlayerChooseGodEvent> observer) {
        currentState.registerRequestPlayerChooseGodEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerRequestPlayerEndTurnEventObserver(Observer)
     */
    @Override
    public void registerRequestPlayerEndTurnEventObserver(Observer<RequestPlayerEndTurnEvent> observer) {
        currentState.registerRequestPlayerEndTurnEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerRequestWorkerBuildBlockEventObserver(Observer)
     */
    @Override
    public void registerRequestWorkerBuildBlockEventObserver(Observer<RequestWorkerBuildBlockEvent> observer) {
        currentState.registerRequestWorkerBuildBlockEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerRequestWorkerBuildDomeEventObserver(Observer)
     */
    @Override
    public void registerRequestWorkerBuildDomeEventObserver(Observer<RequestWorkerBuildDomeEvent> observer) {
        currentState.registerRequestWorkerBuildDomeEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerRequestWorkerForceEventObserver(Observer)
     */
    @Override
    public void registerRequestWorkerForceEventObserver(Observer<RequestWorkerForceEvent> observer) {
        currentState.registerRequestWorkerForceEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerRequestWorkerMoveEventObserver(Observer)
     */
    @Override
    public void registerRequestWorkerMoveEventObserver(Observer<RequestWorkerMoveEvent> observer) {
        currentState.registerRequestWorkerMoveEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerRequestWorkerSpawnEventObserver(Observer)
     */
    @Override
    public void registerRequestWorkerSpawnEventObserver(Observer<RequestWorkerSpawnEvent> observer) {
        currentState.registerRequestWorkerSpawnEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerChallengerSelectGodsEventObserver(Observer)
     */
    public void registerChallengerSelectGodsEventObserver(Observer<ChallengerSelectGodsEvent> observer) {
        currentState.registerChallengerSelectGodsEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerPlayerChooseGodEventObserver(Observer)
     */
    public void registerPlayerChooseGodEventObserver(Observer<PlayerChooseGodEvent> observer) {
        currentState.registerPlayerChooseGodEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerPlayerLoseEventObserver(Observer)
     */
    public void registerPlayerLoseEventObserver(Observer<PlayerLoseEvent> observer) {
        currentState.registerPlayerLoseEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerPlayerTurnStartEventObserver(Observer)
     */
    public void registerPlayerTurnStartEventObserver(Observer<PlayerTurnStartEvent> observer) {
        currentState.registerPlayerTurnStartEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerPlayerWinEventObserver(Observer)
     */
    public void registerPlayerWinEventObserver(Observer<PlayerWinEvent> observer) {
        currentState.registerPlayerWinEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerWorkerBuildBlockEventObserver(Observer)
     */
    public void registerWorkerBuildBlockEventObserver(Observer<WorkerBuildBlockEvent> observer) {
        currentState.registerWorkerBuildBlockEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerWorkerBuildDomeEventObserver(Observer)
     */
    public void registerWorkerBuildDomeEventObserver(Observer<WorkerBuildDomeEvent> observer) {
        currentState.registerWorkerBuildDomeEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerWorkerForceEventObserver(Observer)
     */
    public void registerWorkerForceEventObserver(Observer<WorkerForceEvent> observer) {
        currentState.registerWorkerForceEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerWorkerMoveEventObserver(Observer)
     */
    public void registerWorkerMoveEventObserver(Observer<WorkerMoveEvent> observer) {
        currentState.registerWorkerMoveEventObserver(observer);
    }

    /**
     * @see ModelEventProvider#registerWorkerSpawnEventObserver(Observer)
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
     * Select the god cards to be used in the current game
     * @param gods The list of the chosen god cards
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse selectGods(List<String> gods) {
        ModelResponse response = currentState.selectGods(gods);
        updateState();
        return response;
    }

    /**
     * Select the god card, between the available gods, to be used by the current player
     * @param god The god card, must be still available
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse chooseGod(String god) {
        ModelResponse response = currentState.chooseGod(god);
        updateState();
        return response;
    }

    /**
     * Spawns a worker at the given position, adding it to the current Player
     * @param position The Coordinates
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse spawnWorker(Coordinates position) {
        ModelResponse response = currentState.spawnWorker(position);
        updateState();
        return response;
    }

    /**
     * Move a worker to another cell
     * @param worker The worker to move
     * @param destination The destination of the worker
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse moveWorker(int worker, Coordinates destination) {
        ModelResponse response = currentState.moveWorker(worker, destination);
        updateState();
        return response;
    }

    /**
     * Build a block in the destination cell
     * @param worker The worker who is building
     * @param destination The destination cell
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse buildBlock(int worker, Coordinates destination) {
        ModelResponse response = currentState.buildBlock(worker, destination);
        updateState();
        return response;
    }

    /**
     * Build a dome in the destination cell
     * @param worker The worker who is building
     * @param destination The destination cell
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse buildDome(int worker, Coordinates destination) {
        ModelResponse response = currentState.buildDome(worker, destination);
        updateState();
        return response;
    }

    /**
     * Force the target to another cell
     * @param worker The worker to use
     * @param target The worker to be forced
     * @param destination The destination of the target
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse forceWorker(int worker, int target, Coordinates destination) {
        ModelResponse response = currentState.forceWorker(worker, target, destination);
        updateState();
        return response;
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

    private void applyCopy(Cell cell, Cell original) {
        cell.setLevel(original.getLevel());
        cell.setDoomed(original.isDoomed());
    }

}
