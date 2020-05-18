package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.RoomInfo;
import it.polimi.ingsw.model.ModelEventProvider;

/**
 * Event sent when a room is full and the related game is starting
 */
public class LobbyGameStartEvent extends AbstractLobbyEvent {

    private final RoomInfo roomInfo;

    public LobbyGameStartEvent(String player, RoomInfo roomInfo) {
        super(player);

        this.roomInfo = roomInfo;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getLobbyGameStartEventObservable().notifyObservers(this);
    }

}
