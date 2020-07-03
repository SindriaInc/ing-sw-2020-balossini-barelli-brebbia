package it.polimi.ingsw.common.event;

import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event sent by a player that wants to join a room
 *
 * View -> Model
 */
public class PlayerJoinRoomEvent extends AbstractPlayerEvent {

    /**
     * The owner of the room
     */
    private final String roomOwner;

    /**
     * Class constructor
     *
     * @param player The player
     * @param roomOwner The owner of the room that the player wants to join
     */
    public PlayerJoinRoomEvent(String player, String roomOwner) {
        super(player);

        this.roomOwner = roomOwner;
    }

    public String getRoomOwner() {
        return roomOwner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getPlayerJoinRoomEventObservable().notifyObservers(this);
    }

}
