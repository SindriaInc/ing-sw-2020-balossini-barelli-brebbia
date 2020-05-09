package it.polimi.ingsw.client.clientstates.inputstates;

import it.polimi.ingsw.client.clientstates.DataTypes;

public abstract class AbstractInputState {

    public DataTypes.GamerData readGamerData() {
        return null;
    }

    public DataTypes.ConnectionData readConnectionData() {
        return null;
    }

    /**
     * Obtain the next state of the input
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractInputState
     */
    public abstract AbstractInputState nextInputState();

}
