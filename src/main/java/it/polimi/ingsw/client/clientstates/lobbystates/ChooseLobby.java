package it.polimi.ingsw.client.clientstates.lobbystates;

public class ChooseLobby extends AbstractLobbyState {

    /**
     * True if the gamer chooses to start a new lobby
     */
    private boolean newLobby;

    @Override
    public AbstractLobbyState nextLobbyState() {
        if (newLobby)
            return new GetNewLobbyPlayerNumber();
        else
            return new EnterLobby();
    }
}
