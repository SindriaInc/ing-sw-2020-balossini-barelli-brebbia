package it.polimi.ingsw.client;

/**
 * The interface of the connection
 */
public interface IClient {

    /**
     * Send a message to the server
     * The message will reach the server in an unspecified amount of time
     * If there's an error while sending the message, the error handlers will be notified
     * @param message The message
     */
    void send(String message);

    /**
     * Register a message handler
     * @param receiver The IMessageHandler
     */
    void registerHandler(IMessageHandler receiver);

    /**
     * Register a error handler
     * @param receiver The IErrorHandler
     */
    void registerHandler(IErrorHandler receiver);

    /**
     * Shut down the client, disconnecting from the server
     */
    void shutdown();

}
