package it.polimi.ingsw.common;

import java.util.List;

public class RoomInfo {

    private final String owner;

    private final List<String> otherPlayers;

    private final int maxPlayers;

    private final boolean simpleGame;

    public RoomInfo(String owner, List<String> otherPlayers, int maxPlayers, boolean simpleGame) {
        this.owner = owner;
        this.otherPlayers = otherPlayers;
        this.maxPlayers = maxPlayers;
        this.simpleGame = simpleGame;
    }

    public String getOwner() {
        return owner;
    }

    public List<String> getOtherPlayers() {
        return otherPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isSimpleGame() {
        return simpleGame;
    }

}
