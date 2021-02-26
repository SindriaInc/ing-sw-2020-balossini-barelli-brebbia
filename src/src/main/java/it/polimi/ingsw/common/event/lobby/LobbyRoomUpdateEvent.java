package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.info.RoomInfo;
import it.polimi.ingsw.model.ModelEventProvider;

/**
 * The latest state for the room
 */
public class LobbyRoomUpdateEvent extends AbstractLobbyEvent {

    /**
     * The updated room information
     */
    private final RoomInfo roomInfo;

    /**
     * Class constructor
     *
     * @param player The player that receives the event
     * @param roomInfo The room information
     */
    public LobbyRoomUpdateEvent(String player, RoomInfo roomInfo) {
        super(player);

        this.roomInfo = roomInfo;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getLobbyRoomUpdateEventObservable().notifyObservers(this);
    }

}
