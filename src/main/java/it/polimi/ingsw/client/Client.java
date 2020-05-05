package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.InputState;

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

    public Client(Boolean isGui) {
        this.currentClientState = new InputState();
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

    public void setName() {
        this.name = currentClientState.readName();
    }

    public void setAge() {
        this.age = currentClientState.readAge();
    }

    public void setServerIP() {
        this.serverIP = currentClientState.readIP();
    }

    public void setServerPort () {
        this.serverPort = currentClientState.readPort();
    }

}
