package it.polimi.ingsw.client.data;

import it.polimi.ingsw.common.info.RoomInfo;

import java.util.List;

public class LobbyData extends AbstractData {

    private final String name;

    private final List<RoomInfo> rooms;

    private final boolean waiting;

    public LobbyData(String lastMessage, String name, List<RoomInfo> rooms) {
        this(lastMessage, name, rooms, false);
    }

    private LobbyData(String lastMessage, String name, List<RoomInfo> rooms, boolean waiting) {
        super(lastMessage);

        this.name = name;
        this.rooms = List.copyOf(rooms);
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

    public LobbyData withWaiting(boolean waiting) {
        return new LobbyData(null, getName(), getRooms(), waiting);
    }


}
