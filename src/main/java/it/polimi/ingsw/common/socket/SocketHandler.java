package it.polimi.ingsw.common.socket;

import it.polimi.ingsw.common.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;

public class SocketHandler {

    /**
     * The actual socket, the connection to the other entity
     */
    private final Socket socket;

    /**
     * The handler for messages coming from the socket
     */
    private final InboundHandler inboundHandler;

    /**
     * The handler for messages to be sent
     */
    private final OutboundHandler outboundHandler;

    /**
     * The consumer that handles packets coming from the socket
     */
    private final BiConsumer<SocketHandler, String> inboundMessageConsumer;

    /**
     * The consumer that handles errors while sending messages
     */
    private final BiConsumer<SocketHandler, String> errorMessageConsumer;

    public SocketHandler(ExecutorService executorService, Socket socket,
                         BiConsumer<SocketHandler, String> inboundMessageConsumer,
                         BiConsumer<SocketHandler, String> errorMessageConsumer) {
        this.socket = socket;
        this.inboundMessageConsumer = inboundMessageConsumer;
        this.errorMessageConsumer = errorMessageConsumer;

        InputStream inputStream;
        OutputStream outputStream;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException exception) {
            Logger.getInstance().exception(exception);
            throw new IllegalArgumentException("Unable to init the socket handler");
        }

        inboundHandler = new InboundHandler(inputStream, this::onMessage);
        outboundHandler = new OutboundHandler(outputStream, this::onError);

        executorService.submit(inboundHandler);
        executorService.submit(outboundHandler);
    }

    /**
     * Obtain the destination address of the socket
     */
    public String getAddress() {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    /**
     * Close the socket, disconnecting the player
     */
    public void shutdown() {
        inboundHandler.shutdown();
        outboundHandler.shutdown();

        try {
            socket.close();
        } catch (IOException exception) {
            Logger.getInstance().warning("Exception while closing socket");
            Logger.getInstance().exception(exception);
        }
    }

    /**
     * Schedules the packet to be sent later to the other entity
     */
    public void schedulePacket(String message) {
        outboundHandler.schedulePacket(message);
    }

    private void onMessage(String message) {
        inboundMessageConsumer.accept(this, message);
    }

    private void onError(String message) {
        errorMessageConsumer.accept(this, message);
    }

}
