package it.polimi.ingsw.client.clientstates.lobbystates;

public class WaitFullRoom extends AbstractLobbyState {

    public AbstractLobbyState nextLobbyState() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return this;
        }
        return null;
    }

}
