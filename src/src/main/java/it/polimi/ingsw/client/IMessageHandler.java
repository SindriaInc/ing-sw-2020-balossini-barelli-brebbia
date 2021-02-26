package it.polimi.ingsw.client;

/**
 * The message handler interface
 */
public interface IMessageHandler {

    /**
     * Respond to a message
     * @param message THe message
     */
    void onMessage(String message);

}
