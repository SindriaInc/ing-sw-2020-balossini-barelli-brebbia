package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractGameState {

    /**
     * The ModelEventProvider, used to register and notify observables
     */
    private final ModelEventProvider modelEventProvider;

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

    public AbstractGameState(ModelEventProvider provider, Board board, List<Player> players) {
        this.modelEventProvider = provider;
        this.board = board;
        this.activePlayers.addAll(players);
    }

    public Game.ModelResponse selectGods(List<String> gods) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse chooseGod(String god) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse spawnWorker(Coordinates position) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse moveWorker(int worker, Coordinates destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse buildBlock(int worker, Coordinates destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse buildDome(int worker, Coordinates destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse forceWorker(int worker, int target, Coordinates destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse endTurn() {
        return Game.ModelResponse.INVALID_STATE;
    }

    public final ModelEventProvider getModelEventProvider() {
        return modelEventProvider;
    }

    public final Board getBoard() {
        return board;
    }

    public final List<Player> getPlayers() {
        return List.copyOf(activePlayers);
    }

    public final List<Player> getOpponents(Player player) {
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
     * If the game has ended, this method must return the winner
     * @return The Player
     */
    public abstract Player getCurrentPlayer();

    /**
     * Obtain the next state of the game
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractGameState
     */
    public abstract AbstractGameState nextState();

}
