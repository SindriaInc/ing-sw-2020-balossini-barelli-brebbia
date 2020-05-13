package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.FactoryPattern;
import it.polimi.ingsw.client.clientstates.inputstates.AbstractInputState;

public class InputState extends AbstractClientState {

    /**
     * The current state of input phase, implementing the available interactions
     */
    private AbstractInputState currentInputState;

    public InputState(FactoryPattern factoryPattern) {
        this.currentInputState = factoryPattern.getGamerData();
    }

    @Override
    public DataTypes.GamerData readGamerData() {
        DataTypes.GamerData gamerData = currentInputState.readGamerData();
        currentInputState = currentInputState.nextInputState();
        return gamerData;
    }

    @Override
    public DataTypes.ConnectionData readConnectionData() {
        DataTypes.ConnectionData connectionData = currentInputState.readConnectionData();
        currentInputState = currentInputState.nextInputState();
        return connectionData;
    }

    @Override
    public AbstractClientState nextClientState(FactoryPattern factoryPattern) {
        if (currentInputState != null)
            return this;
        else
            return new ConnectionState();
    }

    /**
     * Updates the current state
     */
    private void updateInputState() {
        currentInputState = currentInputState.nextInputState();
    }

}
