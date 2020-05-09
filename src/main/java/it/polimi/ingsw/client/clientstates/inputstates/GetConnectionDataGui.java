package it.polimi.ingsw.client.clientstates.inputstates;

import it.polimi.ingsw.client.clientstates.DataTypes;

public class GetConnectionDataGui extends AbstractInputState {

    @Override
    public DataTypes.ConnectionData readConnectionData() {
        return super.readConnectionData();
    }

    @Override
    public AbstractInputState nextInputState() {
        return null;
    }
}
