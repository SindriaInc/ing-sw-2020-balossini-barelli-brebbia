package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.clientstates.inputstates.AbstractInputState;
import it.polimi.ingsw.client.clientstates.inputstates.GetGamerData;

public class InputState extends AbstractClientState {

    /**
     * The current state of input phase, implementing the available interactions
     */
    private AbstractInputState currentInputState;

    public InputState() {
        this.currentInputState = new GetGamerData();
    }

    @Override
    public String readName() {
        return currentInputState.readName();
    }

    @Override
    public int readAge() {
        return currentInputState.readAge();
    }

    @Override
    public String readIP() {
        return currentInputState.readIP();
    }

    @Override
    public int readPort() {
        return currentInputState.readPort();
    }

    @Override
    public AbstractClientState nextClientState() {
        if (currentInputState != null)
            return this;
        else
            return new ConnectionState();
    }

}
