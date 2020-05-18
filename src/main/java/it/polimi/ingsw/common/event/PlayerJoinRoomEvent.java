package it.polimi.ingsw.common.event;

import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event sent by a player that wants to join a room
 *
 * View -> Model
 */
public class PlayerJoinRoomEvent extends AbstractPlayerEvent {

    private final String roomOwner;

    public PlayerJoinRoomEvent(String player, String roomOwner) {
        super(player);

        this.roomOwner = roomOwner;
    }

    public String getRoomOwner() {
        return roomOwner;
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerJoinRoomEventObservable().notifyObservers(this);
    }

}
