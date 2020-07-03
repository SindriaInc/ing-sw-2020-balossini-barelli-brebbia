package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An abstract class that can be implemented by the states. It contains variables and methods that are common to
 * all the implemented states.
 */

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
     *
     * When a player loses it gets removed from this list
     */
    private final List<Player> activePlayers = new ArrayList<>();

    /**
     * List of the players spectating
     *
     * When a player loses it gets added into this list
     */
    private final List<Player> spectatorPlayers = new ArrayList<>();

    /**
     * Class constructor, set model event provider, board and list of players
     *
     * @param provider The model event provider
     * @param board The board
     * @param players The player list
     */
    public AbstractGameState(ModelEventProvider provider, Board board, List<Player> players) {
        this.modelEventProvider = provider;
        this.board = board;
        this.activePlayers.addAll(players);
    }

    /**
     * @see Game#selectGods(List)
     */
    public ModelResponse selectGods(List<String> gods) {
        return ModelResponse.INVALID_STATE;
    }

    /**
     * @see Game#chooseGod(String)
     */
    public ModelResponse chooseGod(String god) {
        return ModelResponse.INVALID_STATE;
    }

    /**
     * @see Game#selectFirst(String)
     */
    public ModelResponse selectFirst(String first) {
        return ModelResponse.INVALID_STATE;
    }

    /**
     * @see Game#spawnWorker(Coordinates)
     */
    public ModelResponse spawnWorker(Coordinates position) {
        return ModelResponse.INVALID_STATE;
    }

    /**
     * @see Game#moveWorker(int, Coordinates)
     */
    public ModelResponse moveWorker(int worker, Coordinates destination) {
        return ModelResponse.INVALID_STATE;
    }

    /**
     * @see Game#buildBlock(int, Coordinates)
     */
    public ModelResponse buildBlock(int worker, Coordinates destination) {
        return ModelResponse.INVALID_STATE;
    }

    /**
     * @see Game#buildDome(int, Coordinates)
     */
    public ModelResponse buildDome(int worker, Coordinates destination) {
        return ModelResponse.INVALID_STATE;
    }

    /**
     * @see Game#forceWorker(int, int, Coordinates)
     */
    public ModelResponse forceWorker(int worker, int target, Coordinates destination) {
        return ModelResponse.INVALID_STATE;
    }

    /**
     * @see Game#endTurn()
     */
    public ModelResponse endTurn() {
        return ModelResponse.INVALID_STATE;
    }

    /**
     * Handles the player disconnection
     * If the player is a spectator, logging out should have no effect
     * If the player is still in-game, the game is ended
     * @param player The disconnected player
     * @return true if the game has to be ended
     */
    public boolean logout(String player) {
        Optional<Player> spectator = spectatorPlayers.stream().filter(other -> other.getName().equals(player)).findFirst();

        if (spectator.isPresent()) {
            // The disconnected player is a spectator, no need to do anything
            spectatorPlayers.remove(spectator.get());
            return false;
        }

        Optional<Player> active = activePlayers.stream().filter(other -> other.getName().equals(player)).findFirst();
        return active.isPresent();
    }

    public final ModelEventProvider getModelEventProvider() {
        return modelEventProvider;
    }

    public final Board getBoard() {
        return board;
    }

    /**
     * Obtains a list of the active players in that state
     * @return The list of players
     */
    public final List<Player> getPlayers() {
        return List.copyOf(activePlayers);
    }

    /**
     * Get all the players both spectating and playing
     * @return The list of all players
     */
    public final List<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<>(activePlayers);
        allPlayers.addAll(spectatorPlayers);
        return allPlayers;
    }

    /**
     * Get all the opponents
     * @param player The player
     * @return The opponent list
     */
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
