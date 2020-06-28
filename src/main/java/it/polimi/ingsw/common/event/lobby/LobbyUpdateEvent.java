package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.info.RoomInfo;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * The latest lobby state
 */
public class LobbyUpdateEvent extends AbstractLobbyEvent {

    private final List<String> freePlayers;

    private final List<RoomInfo> rooms;

    private final int minGamePlayers;

    private final int maxGamePlayers;

    public LobbyUpdateEvent(String player, List<String> freePlayers, List<RoomInfo> rooms, int minGamePlayers, int maxGamePlayers) {
        super(player);

        this.freePlayers = freePlayers;
        this.rooms = rooms;
        this.minGamePlayers = minGamePlayers;
        this.maxGamePlayers = maxGamePlayers;
    }

    public List<String> getFreePlayers() {
        return freePlayers;
    }

    public List<RoomInfo> getRooms() {
        return rooms;
    }

    public int getMinGamePlayers() {
        return minGamePlayers;
    }

    public int getMaxGamePlayers() {
        return maxGamePlayers;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getLobbyUpdateEventObservable().notifyObservers(this);
    }

}
