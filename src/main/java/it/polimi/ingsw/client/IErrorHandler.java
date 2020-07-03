package it.polimi.ingsw.client;

import it.polimi.ingsw.client.message.ErrorMessage;

/**
 * The error handler interface
 */
public interface IErrorHandler {

    /**
     * Respond to an error message
     * @param message The error message
     */
    void onError(ErrorMessage message);

}
