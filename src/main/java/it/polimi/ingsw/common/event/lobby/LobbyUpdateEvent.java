package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.RoomInfo;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * The latest lobby state
 */
public class LobbyUpdateEvent extends AbstractLobbyEvent {

    private final List<String> freePlayers;

    private final List<RoomInfo> rooms;

    public LobbyUpdateEvent(String player, List<String> freePlayers, List<RoomInfo> rooms) {
        super(player);

        this.freePlayers = freePlayers;
        this.rooms = rooms;
    }

    public List<String> getFreePlayers() {
        return freePlayers;
    }

    public List<RoomInfo> getRooms() {
        return rooms;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getLobbyUpdateEventObservable().notifyObservers(this);
    }

}
