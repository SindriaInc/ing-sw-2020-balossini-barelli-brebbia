package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class SocketHandler {

    private final Socket socket;

    private final InboundHandler inboundHandler;
    private final OutboundHandler outboundHandler;

    public SocketHandler(String tempName, ExecutorService executorService, Socket socket, IInboundMessageReader inboundMessageReader, IErrorMessageReader errorMessageReader) {
        this.socket = socket;

        inboundHandler = new InboundHandler(tempName, socket, inboundMessageReader);
        outboundHandler = new OutboundHandler(socket, errorMessageReader);

        executorService.submit(inboundHandler);
        executorService.submit(outboundHandler);
    }

    public String getPlayer() {
        return inboundHandler.getPlayer();
    }

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

    public void schedulePacket(OutboundMessage message) {
        outboundHandler.schedulePacket(message);
    }

}
