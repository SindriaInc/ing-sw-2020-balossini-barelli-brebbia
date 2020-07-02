package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.client.data.RoomData;
import it.polimi.ingsw.common.info.RoomInfo;
import it.polimi.ingsw.common.event.lobby.LobbyGameStartEvent;
import it.polimi.ingsw.common.event.lobby.LobbyRoomUpdateEvent;

public class RoomState extends AbstractClientState {

    /**
     * The data used by this state
     */
    private RoomData data;

    public RoomState(ClientConnector clientConnector, String player, RoomInfo room) {
        super(clientConnector);

        this.data = new RoomData(null, player, room);

        getModelEventProvider().registerLobbyRoomUpdateEventObserver(this::onLobbyRoomUpdate);
        getModelEventProvider().registerLobbyGameStartEventObserver(this::onLobbyGameStart);
        updateView();
    }

    public RoomData getData() {
        return data;
    }

    /**
     * Update data and view on a room update event
     * @param event The event
     */
    private void onLobbyRoomUpdate(LobbyRoomUpdateEvent event) {
        data = new RoomData(null, data.getName(), event.getRoomInfo());
        updateView();
    }

    /**
     * Update the game state on a game starts event
     * @param event The event
     */
    private void onLobbyGameStart(LobbyGameStartEvent event) {
        getClientConnector().updateState(new GameState(getClientConnector(), event.getPlayer(), event.getRoomInfo().getOtherPlayers(), event.getRoomInfo().isSimpleGame()));
    }

    /**
     * Update the view
     */
    private void updateView() {
        getClientConnector().getViewer().viewRoom(this);
    }

}
