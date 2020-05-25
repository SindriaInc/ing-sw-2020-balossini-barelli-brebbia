package it.polimi.ingsw.common.info;

import java.util.List;
import java.util.Objects;

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

    public int getPlayersCount() {
        return otherPlayers.size() + 1;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        RoomInfo roomInfo = (RoomInfo) object;
        return maxPlayers == roomInfo.maxPlayers &&
                simpleGame == roomInfo.simpleGame &&
                Objects.equals(owner, roomInfo.owner) &&
                Objects.equals(otherPlayers, roomInfo.otherPlayers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, otherPlayers, maxPlayers, simpleGame);
    }

}
