package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.InputState;
import it.polimi.ingsw.client.clientstates.DataTypes;

public class Client {

    /**
     * The current state of client, implementing the available interactions
     */
    private AbstractClientState currentClientState;

    /**
     * Player name
     */
    private String name;

    /**
     * Player age
     */
    private int age;

    /**
     * Server IP address
     */
    private String serverIP;

    /**
     * Server port
     */
    private int serverPort;

    /**
     * The factory pattern
     */
    private FactoryPattern factory;

    public Client(FactoryPattern factoryPattern) {
        this.factory = factoryPattern;
        this.currentClientState = new InputState(factoryPattern);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setNameAndAge() {
        DataTypes.GamerData gamerData = currentClientState.readGamerData();
        this.name = gamerData.getName();
        this.age = gamerData.getAge();
    }

    public void setServerData() {
        DataTypes.ConnectionData connectionData = currentClientState.readConnectionData();
        this.serverIP = connectionData.getIp();
        this.serverPort = connectionData.getPort();
    }

    public void updateClientState() {
        currentClientState = currentClientState.nextClientState(factory);
    }

}
