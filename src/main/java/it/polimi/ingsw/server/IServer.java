package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.OutboundMessage;

public interface IServer {

    void send(OutboundMessage message);

    void registerHandler(IMessageHandler receiver);

    void registerHandler(IErrorHandler receiver);

    void shutdown();

    void tick();

}
