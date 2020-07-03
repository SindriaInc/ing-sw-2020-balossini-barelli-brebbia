package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The class representing a room of the game, which the player can join or leave to start a game.
 * A player can join a room or create one, and in this case decide the type of game and the number of players.
 * When the room is full the game starts.
 */
public class Room {

    private final Player owner;

    private final List<Player> otherPlayers = new ArrayList<>();

    private final int maxPlayers;

    private final boolean simpleGame;

    /**
     * Class constructor
     * @param owner The room owner
     * @param maxPlayers The max number of players
     * @param simpleGame true if the game is a simple game
     */
    public Room(Player owner, int maxPlayers, boolean simpleGame) {
        this.owner = owner;
        this.maxPlayers = maxPlayers;
        this.simpleGame = simpleGame;
    }

    /**
     * Adds a player to the room
     * @param player The joining player
     */
    public void addPlayer(Player player) {
        otherPlayers.add(player);
    }

    /**
     * Removes a player to the room
     * @param player The leaving player
     */
    public void removePlayer(Player player) {
        otherPlayers.remove(player);
    }

    public Player getOwner() {
        return owner;
    }

    /**
     * Obtains the other players in the room
     * @return A list of players
     */
    public List<Player> getOtherPlayers() {
        return List.copyOf(otherPlayers);
    }

    /**
     * Obtains a list of all the players in the room
     * @return A list of the players in the room
     */
    public List<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<>();
        allPlayers.add(owner);
        allPlayers.addAll(otherPlayers);
        return allPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isSimpleGame() {
        return simpleGame;
    }

    /**
     * Checks if the room is full
     * @return True if the room has reached its max number of players
     */
    public boolean isFull() {
        return otherPlayers.size() >= maxPlayers - 1;
    }

}
