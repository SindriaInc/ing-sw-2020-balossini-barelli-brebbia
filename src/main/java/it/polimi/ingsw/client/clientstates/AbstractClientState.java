package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.FactoryPattern;

public abstract class AbstractClientState {

    /**
     * Read gamer's data
     * @return The gamers data
     */
    public DataTypes.GamerData readGamerData() {
        return null;
    }

    /**
     * Read the connection data
     * @return The connection data
     */
    public DataTypes.ConnectionData readConnectionData() {
        return null;
    }

    /**
     * Starts a socket connection with server
     * @param ip The ip address
     * @param port The port
     */
    public void connectToServer(String ip, int port) {}

    /**
     * Read new lobby data
     * @return The new lobby data
     */
    public DataTypes.LobbyData readLobbyData () {
        return null;
    }



    /**
     * Obtain the next state of the client
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractClientState
     */
    public abstract AbstractClientState nextClientState(FactoryPattern factoryPattern);

}
