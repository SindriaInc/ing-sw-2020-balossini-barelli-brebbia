package it.polimi.ingsw.client.clientstates.inputstates;

import it.polimi.ingsw.client.clientstates.DataTypes;

import java.util.Scanner;

public class GetGamerDataCli extends GetGamerData {

    @Override
    public DataTypes.GamerData readGamerData() {
        DataTypes.GamerData gamerData = new DataTypes.GamerData();

        Scanner data = new Scanner(System.in);

        gamerData.setName(data.next());
        gamerData.setAge(data.nextInt());

        return gamerData;
    }

    @Override
    public AbstractInputState nextInputState() {
        return new GetConnectionDataCli();
    }

}
