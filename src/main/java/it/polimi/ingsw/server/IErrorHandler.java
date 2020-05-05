package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.ErrorMessage;

public interface IErrorHandler {

    void onError(ErrorMessage message);

}
