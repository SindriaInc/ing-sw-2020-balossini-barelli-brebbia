package it.polimi.ingsw.client.clientstates.lobbystates;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.DataTypes;

import java.util.Scanner;

public abstract class AbstractLobbyState {

    public DataTypes.LobbyData readLobbyData() {
        return null;
    }

    /**
     * Obtain the next state of the lobby phase
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractLobbyState
     */
    public abstract AbstractLobbyState nextLobbyState();

}
