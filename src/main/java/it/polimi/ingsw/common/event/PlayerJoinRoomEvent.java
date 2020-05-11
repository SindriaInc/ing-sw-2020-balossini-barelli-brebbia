package it.polimi.ingsw.common.event;

/**
 * Event sent by a player that wants to join a room
 *
 * View -> Model
 */
public class PlayerJoinRoomEvent extends AbstractPlayerEvent {

    private String roomOwner;

    public PlayerJoinRoomEvent(String player, String roomOwner) {
        super(player);

        this.roomOwner = roomOwner;
    }

    public String getRoomOwner() {
        return roomOwner;
    }

}
