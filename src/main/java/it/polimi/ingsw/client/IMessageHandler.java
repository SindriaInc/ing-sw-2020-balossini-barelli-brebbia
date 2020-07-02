package it.polimi.ingsw.client;

public interface IMessageHandler {

    /**
     * Respond to a message
     * @param message THe message
     */
    void onMessage(String message);

}
