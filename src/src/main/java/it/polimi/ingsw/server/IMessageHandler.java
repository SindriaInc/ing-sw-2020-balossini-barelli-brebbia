package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.InboundMessage;

/**
 * Handles a message received from a player
 */
public interface IMessageHandler {

    /**
     * Called when the server receives a message from a player
     * @param message The InboundMessage
     */
    void onMessage(InboundMessage message);

}
