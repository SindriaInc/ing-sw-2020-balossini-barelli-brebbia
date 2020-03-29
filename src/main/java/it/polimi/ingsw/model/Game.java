package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    /**
     * The Board of this game
     */
    private Board board = new Board();

    /**
     * A flag that indicates if the game is a simple game or not.
     * A simple game is a game played without god cards
     */
    private boolean simpleGame;

    /**
     * List of the god cards chosen for the current game
     */
    private final List<God> availableGods = new ArrayList<>();

    /**
     * List of the players in the game
     */
    private final List<Player> players = new ArrayList<>();

    public Board getBoard() {
        return board;
    }

    /**
     * Get the current player
     * @return The player
     */
    // TODO: Implement method
    public Player getCurrentPlayer() { return null; }

    public List<Player> getPlayers() {
        return List.copyOf(players);
    }

    /**
     *  Get the list of the other players
     * @return The list of players
     */
    public List<Player> getOpponents() {
        List<Player> opponents = getPlayers();
        players.remove(getCurrentPlayer());

        return opponents;
    }

    /**
     * Starts the setup phase of the game
     */
    // TODO: Implement method
    public void preStartGame () {}

    /**
     * Select the simple version of the game
     * @param isSimple Flag true for a simple game, false if not
     */
    // TODO: Implement method
    public void selectSimpleGame (boolean isSimple) {}

    /**
     * Select the god cards to be used in the current game
     * @param godList The list of all the god cards
     */
    // TODO: Implement method
    public void selectGods (List<God> godList) {}

    public List<God> getAvailableGods() {
        return List.copyOf(availableGods);
    }

    /**
     * Select your god card between the available gods
     * @param player the player currently choosing
     * @param god the still available god cards
     */
    // TODO: Implement method
    public void chooseGod(Player player, God god) {}

    /**
     * Spawns the workers on the board
     * @param workers List of the workers to spawn
     */
    // TODO: Implement method
    public void spawnWorkers(List<Worker> workers) {}

    /**
     * Starts the real game
     */
    // TODO: Implement method
    public void startGame() {}

    /**
     * Starts the turn of a player
     * @param currentPlayer The player whose turn is starting
     */
    // TODO: Implement method
    public void startTurn(Player currentPlayer) {}

    /**
     * Get the available moves for a worker
     * @param worker The worker to move
     */
    // TODO: Implement method
    public List<Cell> getAvailableMoves(Worker worker) { return null; }

    /**
     * Move a worker to another cell
     * @param worker The worker to move
     * @param destination The destination of the worker
     */
    // TODO: Implement method
    public void moveWorker(Worker worker, Cell destination) {}

    /**
     * Get the available builds for a worker
     * @param worker The worker building the block
     */
    // TODO: Implement method
    public List<Cell> getAvailableBlockBuilds(Worker worker) { return null; }

    /**
     * Build a block in the destination cell
     * @param worker The worker who is building
     * @param destination The destination cell
     */
    // TODO: Implement method
    public void buildBlock(Worker worker, Cell destination) {}

    /**
     * Get the available dome builds for a worker
     * @param worker The worker building the block
     */
    // TODO: Implement method
    public List<Cell> getAvailableDomeBuilds(Worker worker) { return null; }

    /**
     * Build a dome in the destination cell
     * @param worker The worker who is building
     * @param destination The destination cell
     */
    // TODO: Implement method
    public void buildDome(Worker worker, Cell destination) {}

    /**
     * Ends the current turn
     */
    // TODO: Implement method
    public void endTurn() {}

    /**
     * Ends the game
     */
    // TODO: Implement method
    public void endGame() {}

}
