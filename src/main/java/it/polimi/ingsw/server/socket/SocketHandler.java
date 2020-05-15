package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.server.message.ErrorMessage;
import it.polimi.ingsw.server.message.InboundMessage;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class SocketHandler {

    /**
     * The actual socket, the connection to the player
     */
    private final Socket socket;

    /**
     * The handler for messages coming from the player
     */
    private final InboundHandler inboundHandler;

    /**
     * The handler for messages to be sent to the player
     */
    private final OutboundHandler outboundHandler;

    public SocketHandler(String tempName, ExecutorService executorService, Socket socket,
                         Consumer<InboundMessage> inboundMessageConsumer,
                         Consumer<ErrorMessage> errorMessageConsumer) {
        this.socket = socket;

        InputStream inputStream;
        OutputStream outputStream;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException exception) {
            Logger.getInstance().exception(exception);
            throw new IllegalArgumentException("Unable to init the socket handler");
        }

        inboundHandler = new InboundHandler(inputStream, tempName, inboundMessageConsumer);
        outboundHandler = new OutboundHandler(outputStream, errorMessageConsumer);

        executorService.submit(inboundHandler);
        executorService.submit(outboundHandler);
    }

    public String getPlayer() {
        return inboundHandler.getPlayer();
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

        String playerInfo = "";

        if (IServer.isIdentified(getPlayer())) {
            playerInfo = " (Identified as " + getPlayer() + ")";
        } else {
            playerInfo = " (Temporarily known as " + getPlayer() + ")";
        }

        Logger.getInstance().debug("Disconnected " + socket.getInetAddress().getHostAddress() + playerInfo);
    }

    /**
     * @see IServer#send(OutboundMessage)
     */
    public void schedulePacket(OutboundMessage message) {
        outboundHandler.schedulePacket(message);
    }

}
