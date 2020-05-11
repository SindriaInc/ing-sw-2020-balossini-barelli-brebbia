package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.RoomInfo;

public class LobbyGameStartEvent extends LobbyRoomUpdateEvent {

    public LobbyGameStartEvent(String player, RoomInfo roomInfo) {
        super(player, roomInfo);
    }

}
