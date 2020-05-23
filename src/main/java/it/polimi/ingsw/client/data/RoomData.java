package it.polimi.ingsw.client.data;

import it.polimi.ingsw.common.info.RoomInfo;

public class RoomData extends AbstractData {

    private final String name;

    private final RoomInfo room;

    public RoomData(String lastMessage, String name, RoomInfo room) {
        super(lastMessage);

        this.name = name;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public RoomInfo getRoom() {
        return room;
    }


}
