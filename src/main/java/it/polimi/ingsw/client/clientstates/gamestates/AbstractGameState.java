package it.polimi.ingsw.client.clientstates.gamestates;

import it.polimi.ingsw.client.clientstates.AbstractClientState;

public abstract class AbstractGameState {

    /**
     * Obtain the next state of the client
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractClientState
     */
    public abstract AbstractClientState nextClientState();

}
