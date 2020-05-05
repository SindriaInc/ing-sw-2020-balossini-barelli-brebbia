package it.polimi.ingsw.client.clientstates.inputstates;

import it.polimi.ingsw.client.clientstates.AbstractClientState;

public abstract class AbstractInputState {

    public String readName() {
        return null;
    }

    public int readAge() {
        return 0;
    }

    public String readIP() {
        return null;
    }

    public int readPort() {
        return 0;
    }

    /**
     * Obtain the next state of the input
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractInputState
     */
    public abstract AbstractInputState nextInputState();

}
