package it.polimi.ingsw.client.clientstates;

public class ConnectionState extends AbstractClientState {

    public void connectToServer() {

    }

    @Override
    public AbstractClientState nextClientState() {
        return new LobbyState();
    }
}
