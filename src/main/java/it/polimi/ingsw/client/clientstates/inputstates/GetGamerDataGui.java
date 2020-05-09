package it.polimi.ingsw.client.clientstates.inputstates;

import it.polimi.ingsw.client.clientstates.DataTypes;

public class GetGamerDataGui extends GetGamerData {

    @Override
    public DataTypes.GamerData readGamerData() {
        return super.readGamerData();
    }

    @Override
    public AbstractInputState nextInputState() {
        return new GetConnectionDataGui();
    }
}
