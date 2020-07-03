package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.ErrorMessage;

/**
 * Handles an error message produced by the server related to a message sent to a player
 */
public interface IErrorHandler {

    /**
     * Called after a server error, see ErrorType for more information
     * @param message The ErrorMessage
     */
    void onError(ErrorMessage message);

}
