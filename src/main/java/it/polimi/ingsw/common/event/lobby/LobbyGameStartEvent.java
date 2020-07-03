package it.polimi.ingsw.common.event.lobby;

import it.polimi.ingsw.common.info.RoomInfo;
import it.polimi.ingsw.model.ModelEventProvider;

/**
 * Event sent when a room is full and the related game is starting
 */
public class LobbyGameStartEvent extends AbstractLobbyEvent {

    /**
     * The room information that will be used to start the game
     */
    private final RoomInfo roomInfo;

    /**
     * Class constructor
     *
     * @param player The player that receives the event
     * @param roomInfo The room information
     */
    public LobbyGameStartEvent(String player, RoomInfo roomInfo) {
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
        provider.getLobbyGameStartEventObservable().notifyObservers(this);
    }

}
