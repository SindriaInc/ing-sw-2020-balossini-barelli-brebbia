package it.polimi.ingsw.client;

public interface IClient {

    String CONNECT_MESSAGE = "!connected";

    void send(String message);

    void registerHandler(IMessageHandler receiver);

    void registerHandler(IErrorHandler receiver);

    void shutdown();

}
