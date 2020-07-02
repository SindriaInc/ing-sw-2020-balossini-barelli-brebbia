package it.polimi.ingsw.client.data;

import it.polimi.ingsw.common.info.RoomInfo;

/**
 * Represent the data of the room state of the game
 */
public class RoomData extends AbstractData {

    /**
     * The player's name
     */
    private final String name;

    /**
     * The room info
     */
    private final RoomInfo room;

    /**
     * Class constructor, set last message, player's name and room info
     *
     * @param lastMessage The last message
     * @param name The player's name
     * @param room The room info
     */
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
