package it.polimi.ingsw.client.data;

import it.polimi.ingsw.common.info.RoomInfo;

import java.util.List;

public class LobbyData extends AbstractData {

    private final String name;

    private final List<RoomInfo> rooms;

    private final boolean waiting;

    private final int minGamePlayers;

    private final int maxGamePlayers;

    public LobbyData(String lastMessage, String name, List<RoomInfo> rooms, int minGamePlayers, int maxGamePlayers) {
        this(lastMessage, name, rooms, minGamePlayers, maxGamePlayers, false);
    }

    private LobbyData(String lastMessage, String name, List<RoomInfo> rooms, int minGamePlayers, int maxGamePlayers, boolean waiting) {
        super(lastMessage);

        this.name = name;
        this.rooms = List.copyOf(rooms);
        this.minGamePlayers = minGamePlayers;
        this.maxGamePlayers = maxGamePlayers;
        this.waiting = false;
    }

    public String getName() {
        return name;
    }

    public List<RoomInfo> getRooms() {
        return rooms;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public int getMinGamePlayers() {
        return minGamePlayers;
    }

    public int getMaxGamePlayers() {
        return maxGamePlayers;
    }

    public LobbyData withWaiting(boolean waiting) {
        return new LobbyData(null, getName(), getRooms(), minGamePlayers, maxGamePlayers, waiting);
    }

}
