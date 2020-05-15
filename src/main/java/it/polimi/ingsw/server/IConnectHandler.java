package it.polimi.ingsw.server;

public interface IConnectHandler {

    /**
     * Called when the server accepts a new player
     * @param tempName The fake temporary name, used to identify the player while not logged in
     */
    void onConnect(String tempName);

}
