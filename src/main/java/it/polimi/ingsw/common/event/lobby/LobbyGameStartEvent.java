package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.RoomInfo;

public class LobbyGameStartEvent extends AbstractLobbyEvent {

    private final RoomInfo roomInfo;

    public LobbyGameStartEvent(String player, RoomInfo roomInfo) {
        super(player);

        this.roomInfo = roomInfo;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

}
