package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.message.ErrorMessage;

public interface IErrorMessageReader {

    void scheduleRead(ErrorMessage message);

}
