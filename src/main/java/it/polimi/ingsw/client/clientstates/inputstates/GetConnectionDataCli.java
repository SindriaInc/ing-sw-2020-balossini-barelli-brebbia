package it.polimi.ingsw.client.clientstates.inputstates;

import it.polimi.ingsw.client.clientstates.DataTypes;

import java.util.Scanner;

public class GetConnectionDataCli extends AbstractInputState {

    @Override
    public DataTypes.ConnectionData readConnectionData() {
        DataTypes.ConnectionData connectionData = new DataTypes.ConnectionData();

        Scanner server = new Scanner(System.in);

        connectionData.setIp(server.next());
        connectionData.setPort(server.nextInt());

        return connectionData;
    }

    @Override
    public AbstractInputState nextInputState() {
        return null;
    }

}
