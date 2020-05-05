package it.polimi.ingsw.client.clientstates.lobbystates;

public class CreateLobby extends AbstractLobbyState {

    @Override
    public AbstractLobbyState nextLobbyState() {
        return new WaitFullRoom();
    }
}
