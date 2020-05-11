package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.RoomInfo;

import java.util.List;

/**
 * The latest lobby state
 */
public class LobbyUpdateEvent extends AbstractLobbyEvent {

    private final List<RoomInfo> rooms;

    public LobbyUpdateEvent(String player, List<RoomInfo> rooms) {
        super(player);

        this.rooms = rooms;
    }

    public List<RoomInfo> getRooms() {
        return rooms;
    }

}
