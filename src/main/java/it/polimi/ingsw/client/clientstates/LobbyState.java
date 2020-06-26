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

public class LobbyState extends AbstractClientState {

    /**
     * The data used by this state
     */
    private LobbyData data;

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

    public void acceptJoin(String room) {
        data = data.withWaiting(true);
        updateView();
        getClientConnector().send(new PlayerJoinRoomEvent(data.getName(), room));
    }

    public void acceptCreate(boolean simple, int maxPlayers) {
        data = data.withWaiting(true);
        updateView();
        getClientConnector().send(new PlayerCreateRoomEvent(data.getName(), maxPlayers, simple));
    }

    private void onLobbyUpdate(LobbyUpdateEvent event) {
        data = new LobbyData(data.getLastMessage().orElse(null),
                data.getName(), event.getRooms(), event.getMinGamePlayers(), event.getMaxGamePlayers());
        updateView();
    }

    private void onLobbyRoomUpdate(LobbyRoomUpdateEvent event) {
        getClientConnector().updateState(new RoomState(getClientConnector(), event.getPlayer(), event.getRoomInfo()));
    }

    private void onResponse(AbstractResponseEvent event) {
        data = new LobbyData("Couldn't join or create the room, are the parameters correct?",
                data.getName(), data.getRooms(), data.getMinGamePlayers(), data.getMaxGamePlayers());
        updateView();
    }

    private void updateView() {
        getClientConnector().getViewer().viewLobby(this);
    }

}