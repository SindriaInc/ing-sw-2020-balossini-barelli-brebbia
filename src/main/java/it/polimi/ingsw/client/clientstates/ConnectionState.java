package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.FactoryPattern;

import java.io.IOException;
import java.net.Socket;

public class ConnectionState extends AbstractClientState {

    /**
     * Last resource for IP address
     */
    private String thuleIP = "127.0.0.1";

    /**
     * Last resource for port
     */
    private int thulePort = 4242;

    public void connectToServer(String ip, int port) {
        Socket serverSocket = null;

        try {
            serverSocket = new Socket(ip, port);
            System.out.println("Connecting...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractClientState nextClientState(FactoryPattern factoryPattern) {
        return new LobbyState(factoryPattern);
    }
}
