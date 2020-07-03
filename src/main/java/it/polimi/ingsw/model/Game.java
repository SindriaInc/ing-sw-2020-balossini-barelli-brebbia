package it.polimi.ingsw.model;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.IModelEventProvider;
import it.polimi.ingsw.model.gamestates.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The class representing the game intended has the sequence of states and action that can be performed. The
 * game is initialized and after every turn its state it's updated, following the action of the players and the
 * game phases as stated by the rules.
 */
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
     * The game will have only have the ModelEventProvider, Game#init needs to be called to start the game
     */
    public Game(ModelEventProvider provider) {
        currentState = new PreInitGame(provider);
    }

    /**
     * Initialize the game, defining the initial state
     * @param players The list of players
     * @param deck The deck, containing the list of Gods
     * @param simpleGame True for a simple game, skipping god selection
     */
    public void init(List<Player> players, Deck deck, boolean simpleGame) {
        Board board = new Board(BOARD_ROWS, BOARD_COLUMNS);

        if (simpleGame) {
            currentState = new PreWorkersGame(currentState.getModelEventProvider(), board, players, MAX_WORKERS);
        } else {
            currentState = new PreGodsGame(currentState.getModelEventProvider(), board, players, MAX_WORKERS, deck.getGods());
        }
    }

    /**
     * Obtain the IModelEventProvider, to be used to register model event observers
     */
    public IModelEventProvider getModelEventProvider() {
        return currentState.getModelEventProvider();
    }

    /**
     * Obtain a copy of the game players
     * @return The players
     *
     * <strong>This method has no side effect</strong>
     */
    public List<Player> getAllPlayers() {
        return List.copyOf(currentState.getAllPlayers());
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
     * Select the first player that will be able to spawn workers
     * @param first The first player
     * @return the response - If the arguments pass the related check, the response will be ALLOW
     */
    public ModelResponse selectFirst(String first) {
        ModelResponse response = currentState.selectFirst(first);
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
     * Handles a player disconnection
     * Does not return anything since a disconnection can't be cancelled
     * @param player The player
     */
    public void logout(String player) {
        if (!currentState.logout(player)) {
            return;
        }

        // Forcibly end the game
        List<Player> players = new ArrayList<>(currentState.getAllPlayers());
        players.removeIf(other -> other.getName().equals(player));
        currentState = new EndGame(currentState.getModelEventProvider(), currentState.getBoard(), null, players);
    }

    /**
     * Obtains the board of the game
     * @return The board in the current state
     */
    protected Board getOriginalBoard() {
        return currentState.getBoard();
    }

    private void updateState() {
        currentState = currentState.nextState();
    }

}
