package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * List of the players spectating
     *
     * When a player loses it gets added into this list
     */
    private final List<Player> spectatorPlayers = new LinkedList<>();

    public AbstractGameState(ModelEventProvider provider, Board board, List<Player> players) {
        this.modelEventProvider = provider;
        this.board = board;
        this.activePlayers.addAll(players);
    }

    public ModelResponse selectGods(List<String> gods) {
        return ModelResponse.INVALID_STATE;
    }

    public ModelResponse chooseGod(String god) {
        return ModelResponse.INVALID_STATE;
    }

    public ModelResponse spawnWorker(Coordinates position) {
        return ModelResponse.INVALID_STATE;
    }

    public ModelResponse moveWorker(int worker, Coordinates destination) {
        return ModelResponse.INVALID_STATE;
    }

    public ModelResponse buildBlock(int worker, Coordinates destination) {
        return ModelResponse.INVALID_STATE;
    }

    public ModelResponse buildDome(int worker, Coordinates destination) {
        return ModelResponse.INVALID_STATE;
    }

    public ModelResponse forceWorker(int worker, int target, Coordinates destination) {
        return ModelResponse.INVALID_STATE;
    }

    public ModelResponse endTurn() {
        return ModelResponse.INVALID_STATE;
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

    public final List<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<>(activePlayers);
        allPlayers.addAll(spectatorPlayers);
        return allPlayers;
    }

    public final List<Player> getOpponents(Player player) {
        List<Player> opponents = new ArrayList<>(activePlayers);
        opponents.remove(player);

        return opponents;
    }

    /**
     * Set the receivers of an event as every player in the game
     */
    final void setReceivers(AbstractEvent event) {
        List<String> receivers = getAllPlayers().stream().map(Player::getName).collect(Collectors.toList());
        event.setReceivers(receivers);
    }

    /**
     * Remove the player from the players list
     * The player is added as a spectator
     */
    final void removePlayer(Player player) {
        this.activePlayers.remove(player);
        spectatorPlayers.add(player);
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
