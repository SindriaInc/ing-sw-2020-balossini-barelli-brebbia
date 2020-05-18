package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.ErrorMessage;

public interface IErrorHandler {

    /**
     * Called after a server error, see ErrorType for more information
     * @param message The ErrorMessage
     */
    void onError(ErrorMessage message);

}
