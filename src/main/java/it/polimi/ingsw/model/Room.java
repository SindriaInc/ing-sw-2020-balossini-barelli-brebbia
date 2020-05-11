package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private final Player owner;

    private final List<Player> otherPlayers = new ArrayList<>();

    private final int maxPlayers;

    private final boolean simpleGame;

    public Room(Player owner, int maxPlayers, boolean simpleGame) {
        this.owner = owner;
        this.maxPlayers = maxPlayers;
        this.simpleGame = simpleGame;
    }

    public void addPlayer(Player player) {
        otherPlayers.add(player);
    }

    public void removePlayer(Player player) {
        otherPlayers.remove(player);
    }

    public Player getOwner() {
        return owner;
    }

    public List<Player> getOtherPlayers() {
        return List.copyOf(otherPlayers);
    }

    public List<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<>(otherPlayers);
        allPlayers.add(owner);
        return allPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isSimpleGame() {
        return simpleGame;
    }

    public boolean isFull() {
        return otherPlayers.size() >= maxPlayers - 1;
    }

}
