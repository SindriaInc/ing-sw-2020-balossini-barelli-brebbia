package it.polimi.ingsw.client.clientstates;


import it.polimi.ingsw.client.clientstates.lobbystates.AbstractLobbyState;
import it.polimi.ingsw.client.clientstates.lobbystates.ChooseLobby;

public class LobbyState extends AbstractClientState {

    /**
     * The current state of lobby phase, implementing the available interactions
     */
    private AbstractLobbyState currentLobbyState;

    /**
     * An eventual new lobby player number
     */
    private int newLobbyPlayerNumber;

    /**
     * An eventual new lobby game type.
     * True if simple game, false if not
     */
    private boolean newLobbyGameType;

    public LobbyState() {
        this.currentLobbyState = new ChooseLobby();
    }

    @Override
    public AbstractClientState nextClientState() {
        if (currentLobbyState != null)
            return this;
        else
            return new GameState();
    }

    public int getNewLobbyPlayerNumber() {
        return newLobbyPlayerNumber;
    }

    public void setNewLobbyPlayerNumber(int newLobbyPlayerNumber) {
        this.newLobbyPlayerNumber = currentLobbyState.readPlayerNumber();
    }

    public boolean isNewLobbyGameType() {
        return newLobbyGameType;
    }

    public void setNewLobbyGameType(boolean newLobbyGameType) {
        this.newLobbyGameType = currentLobbyState.readGameType();
    }
}