package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.client.message.ErrorMessage;

public interface IErrorMessageReader {

    void scheduleRead(ErrorMessage message);

}
