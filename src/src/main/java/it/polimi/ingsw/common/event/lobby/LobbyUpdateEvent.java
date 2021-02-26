package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.info.RoomInfo;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * The latest lobby state
 */
public class LobbyUpdateEvent extends AbstractLobbyEvent {

    /**
     * The list of players that are neither in a room nor in a game
     */
    private final List<String> freePlayers;

    /**
     * The list of available rooms
     */
    private final List<RoomInfo> rooms;

    /**
     * The minimum number of players in a game
     */
    private final int minGamePlayers;

    /**
     * The maximum number of players in a game
     */
    private final int maxGamePlayers;

    /**
     * Class constructor
     *
     * @param player The player that receives the event
     * @param freePlayers The list of free players
     * @param rooms The list of rooms
     * @param minGamePlayers The minimum number of players in a game
     * @param maxGamePlayers The maximum number of players in a game
     */
    public LobbyUpdateEvent(String player, List<String> freePlayers, List<RoomInfo> rooms, int minGamePlayers, int maxGamePlayers) {
        super(player);

        this.freePlayers = freePlayers;
        this.rooms = rooms;
        this.minGamePlayers = minGamePlayers;
        this.maxGamePlayers = maxGamePlayers;
    }

    /**
     * A copy of the list of free players
     *
     * @return The list
     */
    public List<String> getFreePlayers() {
        return List.copyOf(freePlayers);
    }

    /**
     * A copy of the list of available rooms
     *
     * @return The list
     */
    public List<RoomInfo> getRooms() {
        return List.copyOf(rooms);
    }

    public int getMinGamePlayers() {
        return minGamePlayers;
    }

    public int getMaxGamePlayers() {
        return maxGamePlayers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getLobbyUpdateEventObservable().notifyObservers(this);
    }

}
