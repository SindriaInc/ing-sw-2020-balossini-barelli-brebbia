package it.polimi.ingsw.client;

import it.polimi.ingsw.client.message.ErrorMessage;

public interface IErrorHandler {

    void onError(ErrorMessage message);

}
