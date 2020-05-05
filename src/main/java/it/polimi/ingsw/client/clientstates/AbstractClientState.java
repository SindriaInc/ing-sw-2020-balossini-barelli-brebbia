package it.polimi.ingsw.client.clientstates;

public abstract class AbstractClientState {

    /**
     * Read player's name
     * @return The name
     */
    public String readName() {
        return null;
    }

    /**
     * Read the player's age
     * @return The age
     */
    public int readAge() {
        return 0;
    }

    /**
     * Read the server's IP
     * @return The address
     */
    public String readIP() {
        return null;
    }

    /**
     * Read the server's port
     * @return The port number
     */
    public int readPort() {
        return 0;
    }

    /**
     * Read the new game type
     * @return The game type
     */
    public boolean readGameType () {
        return false;
    }

    /**
     * Read the number of players
     * @return The number
     */
    public int readPlayerNumber () {
        return 0;
    }

    /**
     * Obtain the next state of the client
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractClientState
     */
    public abstract AbstractClientState nextClientState();

}
