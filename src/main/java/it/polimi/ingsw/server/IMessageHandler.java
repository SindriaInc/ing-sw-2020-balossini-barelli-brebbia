package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.InboundMessage;

public interface IMessageHandler {

    void onMessage(InboundMessage message);

}
