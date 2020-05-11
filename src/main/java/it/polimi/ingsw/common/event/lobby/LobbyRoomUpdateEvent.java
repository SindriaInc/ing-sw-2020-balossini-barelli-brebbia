package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.RoomInfo;

/**
 * The latest state for the room
 */
public class LobbyRoomUpdateEvent extends AbstractLobbyEvent {

    private final RoomInfo roomInfo;

    public LobbyRoomUpdateEvent(String player, RoomInfo roomInfo) {
        super(player);

        this.roomInfo = roomInfo;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

}
