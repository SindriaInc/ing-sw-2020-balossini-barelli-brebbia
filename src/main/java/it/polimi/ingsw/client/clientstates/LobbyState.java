package it.polimi.ingsw.client.clientstates;


import it.polimi.ingsw.client.FactoryPattern;
import it.polimi.ingsw.client.clientstates.lobbystates.AbstractLobbyState;

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

    public LobbyState(FactoryPattern factoryPattern) {
        this.currentLobbyState = factoryPattern.chooseLobby();
    }

    @Override
    public AbstractClientState nextClientState(FactoryPattern factoryPattern) {
        if (currentLobbyState != null)
            return this;
        else
            return new GameState();
    }

    public int getNewLobbyPlayerNumber() {
        return newLobbyPlayerNumber;
    }

    public boolean isNewLobbyGameType() {
        return newLobbyGameType;
    }

    @Override
    public DataTypes.LobbyData readLobbyData() {
        DataTypes.LobbyData lobbyData = currentLobbyState.readLobbyData();
        currentLobbyState = currentLobbyState.nextLobbyState();
        return  lobbyData;
    }

}