package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.message.InboundMessage;

public interface IInboundMessageReader {

    void scheduleRead(InboundMessage message);

}
