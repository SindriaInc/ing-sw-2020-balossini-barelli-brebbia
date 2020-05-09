package it.polimi.ingsw.client.clientstates.lobbystates;

public class ChooseLobbyCli extends ChooseLobby {

    /**
     * True if the gamer chooses to start a new lobby
     */
    private boolean newLobby;

    @Override
    public AbstractLobbyState nextLobbyState() {
        if (newLobby)
            return new GetNewLobbyDataGui();
        else
            return new EnterLobby();
    }
}
