package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.*;

import java.util.*;

public abstract class AbstractGameState {

    /**
     * The Board of this game
     */
    private final Board board;

    /**
     * List of the players in the game
     * The order of the players is preserved
     *
     * When a player loses it gets removed from this list
     */
    private final List<Player> activePlayers = new LinkedList<>();

    public AbstractGameState(Board board, List<Player> players) {
        this.board = board;
        this.activePlayers.addAll(players);
    }

    public List<God> getAvailableGods() {
        throw new IllegalStateException();
    }

    public int getSelectGodsCount() {
        throw new IllegalStateException();
    }

    public boolean checkCanSelectGods(List<God> gods) {
        throw new IllegalStateException();
    }

    public void selectGods(List<God> gods) {
        throw new IllegalStateException();
    }

    public void chooseGod(God god) {
        throw new IllegalStateException();
    }

    public List<Cell> getAvailableCells() {
        throw new IllegalStateException();
    }

    public void spawnWorker(Worker worker) {
        throw new IllegalStateException();
    }

    public List<Cell> getAvailableMoves(Worker worker) {
        throw new IllegalStateException();
    }

    public void moveWorker(Worker worker, Cell destination) {
        throw new IllegalStateException();
    }

    public List<Cell> getAvailableBlockBuilds(Worker worker) {
        throw new IllegalStateException();
    }

    public void buildBlock(Worker worker, Cell destination) {
        throw new IllegalStateException();
    }

    public List<Cell> getAvailableDomeBuilds(Worker worker) {
        throw new IllegalStateException();
    }

    public void buildDome(Worker worker, Cell destination) {
        throw new IllegalStateException();
    }

    public List<Cell> getAvailableForces(Worker worker, Worker target) {
        throw new IllegalStateException();
    }

    public void forceWorker(Worker worker, Worker target, Cell destination) {
        throw new IllegalStateException();
    }

    public boolean checkCanEndTurn() {
        throw new IllegalStateException();
    }

    public void endTurn() {
        throw new IllegalStateException();
    }

    public boolean checkHasLost(Player player) {
        return !activePlayers.contains(player);
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return List.copyOf(activePlayers);
    }

    public List<Player> getOpponents(Player player) {
        List<Player> opponents = new ArrayList<>(activePlayers);
        opponents.remove(player);

        return opponents;
    }

    /**
     * Remove the player from the players list
     */
    final void removePlayer(Player player) {
        this.activePlayers.remove(player);
    }

    /**
     * Update the list of players with the given sorted list
     * The list must contain each and every player originally present
     */
    final void sortPlayers(List<Player> players) {
        if (players.size() != this.activePlayers.size() || !players.containsAll(this.activePlayers) || !this.activePlayers.containsAll(players)) {
            throw new IllegalArgumentException("The new player list is not a sort of the original list");
        }

        this.activePlayers.clear();
        this.activePlayers.addAll(players);
    }

    /**
     * Obtain the current player that is able to interact with the game
     * Calling this method repeatedly should not result in a different player unless other methods got called
     * If AbstractGameState#isEnded returns true, this method must return the winner
     * @return The Player
     */
    public abstract Player getCurrentPlayer();

    /**
     * Obtain the next state of the game
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractGameState
     */
    public abstract AbstractGameState nextState();

    /**
     * Check if the game has ended, meaning that one of the player has won
     * If this method returns true, AbstractGameState#getCurrentPlayer must return the winner
     * @return true if there is a winner
     *
     * <strong>This method must have no side effect</strong>
     */
    public abstract boolean isEnded();

}
