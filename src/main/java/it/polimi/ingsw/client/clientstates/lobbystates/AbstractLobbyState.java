package it.polimi.ingsw.client.clientstates.lobbystates;

import it.polimi.ingsw.client.clientstates.AbstractClientState;

import java.util.Scanner;

public abstract class AbstractLobbyState {

    public boolean readGameType() {
        return false;
    }

    public int readPlayerNumber() {
        return 0;
    }

    /**
     * Obtain the next state of the lobby phase
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractLobbyState
     */
    public abstract AbstractLobbyState nextLobbyState();

}
