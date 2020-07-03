package it.polimi.ingsw.common.info;

import java.util.List;
import java.util.Objects;

/**
 * A simplified version of a <code>Room</code>, only containing serializable fields
 * To be used to share information via events
 */
public class RoomInfo {

    /**
     * The owner of the room
     */
    private final String owner;

    /**
     * The list of players in the room, the owner is excluded
     */
    private final List<String> otherPlayers;

    /**
     * The maximum number of players allowed
     */
    private final int maxPlayers;

    /**
     * Whether or not the game to be created is simple
     */
    private final boolean simpleGame;

    /**
     * Class constructor
     *
     * @param owner The owner
     * @param otherPlayers The list of players
     * @param maxPlayers The maximum number of players
     * @param simpleGame Whether the game will be simple
     */
    public RoomInfo(String owner, List<String> otherPlayers, int maxPlayers, boolean simpleGame) {
        this.owner = owner;
        this.otherPlayers = otherPlayers;
        this.maxPlayers = maxPlayers;
        this.simpleGame = simpleGame;
    }

    public String getOwner() {
        return owner;
    }

    /**
     * A copy of the list of players in the room, excluding the owner
     *
     * @return The list of players
     */
    public List<String> getOtherPlayers() {
        return List.copyOf(otherPlayers);
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
