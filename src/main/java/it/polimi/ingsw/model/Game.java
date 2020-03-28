package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    /**
     * A flag  that indicates if the game is a simple game or not.
     * A simple game is a game played without god cards
     */
    private boolean simpleGame;

    /**
     * List of the god cards chosen for the current game
     */
    private List<God> availableGods = new ArrayList<>();

    /**
     * List of the players in the game
     */
    private List<Player> players = new ArrayList<>();


    public List<Player> getPlayers() {
        return players;
    }

    /**
     *  Get the current player
     * @return The player
     */
    public Player getCurrentPlayer() {
        Player currentPlayer;
        return currentPlayer;
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
    public void preStartGame () {}

    /**
     * Select the simple version of the game
     * @param isSimple Flag true for a simple game, false if not
     */
    public void selectSimpleGame (boolean isSimple) {}

    /**
     * Select the god cards to be used in the current game
     * @param godList The list of all the god cards
     */
    public void selectGods (List<God> godList) {}

    public List<God> getAvailableGods() {
        return availableGods;
    }

    /**
     * Select your god card between the available gods
     * @param player the player currently choosing
     * @param god the still available god cards
     */
    public void chooseGod (Player player, God god) {}

    /**
     * Spawns the workers on the board
     * @param workers List of the workers to spawn
     */
    public void spawnWorkers (List<Worker> workers) {}

    /**
     * Starts the real game
     */
    public void startGame () {}

    /**
     * Starts the turn of a player
     * @param currentPlayer The player whose turn is starting
     */
    public void startTurn (Player currentPlayer) {}

    /**
     * Get the available moves for a worker
     * @param worker The worker to move
     */
    public List<Cell> getAvailableMoves (Worker worker) {
        List<Cell> availableMoves = new ArrayList<>();
        return availableMoves;
    }

    /**
     * Move a worker to another cell
     * @param worker The worker to move
     * @param destination The destination of the worker
     */
    public void moveWorker (Worker worker, Cell destination) {}

    /**
     * Get the available builds for a worker
     * @param worker The worker building the block
     */
    public List<Cell> getAvailableBlockBuilds (Worker worker) {
        List<Cell> availableBlockBuilds = new ArrayList<>();
        return availableBlockBuilds;
    }

    /**
     * Build a block in the destination cell
     * @param worker The worker who is building
     * @param destination The destination cell
     */
    public void buildBlock (Worker worker, Cell destination) {}

    /**
     * Get the available dome builds for a worker
     * @param worker The worker building the block
     */
    public List<Cell> getAvailableDomeBuilds (Worker worker) {
        List<Cell> availableDomeBuilds = new ArrayList<>();
        return availableDomeBuilds;
    }

    /**
     * Build a dome in the destination cell
     * @param worker The worker who is building
     * @param destination The destination cell
     */
    public void buildDome (Worker worker, Cell destination) {}

    /**
     * Ends the current turn
     */
    public void endTurn() {}

    /**
     * Ends the game
     */
    public void endGame() {}

}
