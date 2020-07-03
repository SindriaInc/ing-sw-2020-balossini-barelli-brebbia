package it.polimi.ingsw.client.clientstates;


import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.client.data.LobbyData;
import it.polimi.ingsw.common.info.RoomInfo;
import it.polimi.ingsw.common.event.PlayerCreateRoomEvent;
import it.polimi.ingsw.common.event.PlayerJoinRoomEvent;
import it.polimi.ingsw.common.event.lobby.LobbyRoomUpdateEvent;
import it.polimi.ingsw.common.event.lobby.LobbyUpdateEvent;
import it.polimi.ingsw.common.event.response.AbstractResponseEvent;

import java.util.List;

/**
 * Generate the data for the lobby phase of the game
 */
public class LobbyState extends AbstractClientState {

    /**
     * The data used by this state
     */
    private LobbyData data;

    /**
     * Class constructor, set the connector.
     * Create lobby data given player's name, room list, and the limit numbers of players for each room
     * Register the handlers
     *
     * @param clientConnector The client connector
     * @param player The player's name
     * @param rooms The room list
     * @param minGamePlayers Minimum number of player for each room
     * @param maxGamePlayers Maximum number of players for each room
     */
    public LobbyState(ClientConnector clientConnector, String player, List<RoomInfo> rooms, int minGamePlayers, int maxGamePlayers) {
        super(clientConnector);

        this.data = new LobbyData(null, player, rooms, minGamePlayers, maxGamePlayers);

        getModelEventProvider().registerLobbyUpdateEventObserver(this::onLobbyUpdate);
        getModelEventProvider().registerLobbyRoomUpdateEventObserver(this::onLobbyRoomUpdate);
        getResponseEventProvider().registerResponseInvalidParametersEventObserver(this::onResponse);
        getResponseEventProvider().registerResponseInvalidPlayerEventObserver(this::onResponse);
        getResponseEventProvider().registerResponseInvalidStateEventObserver(this::onResponse);
        updateView();
    }

    public LobbyData getData() {
        return data;
    }

    /**
     * Update data and view and send a join room event
     * @param room The room
     */
    public void acceptJoin(String room) {
        data = data.withWaiting(true);
        updateView();
        getClientConnector().send(new PlayerJoinRoomEvent(data.getName(), room));
    }

    /**
     * Update data and view and send a create room event
     * @param simple Whether the game is simple or not
     * @param maxPlayers The player number
     */
    public void acceptCreate(boolean simple, int maxPlayers) {
        data = data.withWaiting(true);
        updateView();
        getClientConnector().send(new PlayerCreateRoomEvent(data.getName(), maxPlayers, simple));
    }

    /**
     * Update data and view on a lobby update event
     * @param event The event
     */
    private void onLobbyUpdate(LobbyUpdateEvent event) {
        data = new LobbyData(data.getLastMessage().orElse(null),
                data.getName(), event.getRooms(), event.getMinGamePlayers(), event.getMaxGamePlayers());
        updateView();
    }

    /**
     * Update data and view on a room update event
     * @param event The event
     */
    private void onLobbyRoomUpdate(LobbyRoomUpdateEvent event) {
        getClientConnector().updateState(new RoomState(getClientConnector(), event.getPlayer(), event.getRoomInfo()));
    }

    /**
     * Update data and view on a response event
     * @param event The event
     */
    private void onResponse(AbstractResponseEvent event) {
        data = new LobbyData("Couldn't join or create the room, are the parameters correct?",
                data.getName(), data.getRooms(), data.getMinGamePlayers(), data.getMaxGamePlayers());
        updateView();
    }

    /**
     * Update the view
     */
    private void updateView() {
        getClientConnector().getViewer().viewLobby(this);
    }

}