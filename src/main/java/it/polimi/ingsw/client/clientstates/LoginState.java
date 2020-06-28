package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.client.data.LoginData;
import it.polimi.ingsw.common.event.PlayerLoginEvent;
import it.polimi.ingsw.common.event.lobby.LobbyUpdateEvent;
import it.polimi.ingsw.common.event.response.AbstractResponseEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidLoginEvent;

public class LoginState extends AbstractClientState {

    /**
     * The data used by this state
     */
    private LoginData data;

    public LoginState(ClientConnector clientConnector) {
        super(clientConnector);

        this.data = new LoginData(null, null, null);

        getModelEventProvider().registerLobbyUpdateEventObserver(this::onLobbyUpdate);
        getResponseEventProvider().registerResponseInvalidLoginEventObserver(this::onLoginResponse);
        getResponseEventProvider().registerResponseInvalidParametersEventObserver(this::onResponse);
        getResponseEventProvider().registerResponseInvalidPlayerEventObserver(this::onResponse);
        getResponseEventProvider().registerResponseInvalidStateEventObserver(this::onResponse);
        updateView();
    }

    public LoginData getData() {
        return data;
    }

    public void acceptName(String name) {
        data = data.withName(name);
        updateView();
    }

    public void acceptAge(int age) {
        data = data.withAge(age);
        updateView();
    }

    public void acceptLogin() {
        if (data.getName().isEmpty() || data.getAge().isEmpty()) {
            throw new IllegalStateException("Tried to login with a null name or age");
        }

        getClientConnector().send(new PlayerLoginEvent(data.getName().get(), data.getAge().get()));
    }

    private void onLoginResponse(ResponseInvalidLoginEvent event) {
        data = new LoginData(event.getMessage(), null, null);
        updateView();
    }

    private void onResponse(AbstractResponseEvent event) {
        data = new LoginData("Invalid name or age", null, null);
        updateView();
    }

    private void onLobbyUpdate(LobbyUpdateEvent event) {
        getClientConnector().updateState(new LobbyState(getClientConnector(), event.getPlayer(), event.getRooms(),
                event.getMinGamePlayers(), event.getMaxGamePlayers()));
    }

    private void updateView() {
        getClientConnector().getViewer().viewLogin(this);
    }

}
