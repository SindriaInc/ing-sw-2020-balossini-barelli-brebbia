package it.polimi.ingsw.client.clientstates.lobbystates;

public class EnterLobby extends AbstractLobbyState {

    @Override
    public AbstractLobbyState nextLobbyState() {
        return new WaitFullRoom();
    }

}
