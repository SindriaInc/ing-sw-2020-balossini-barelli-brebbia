package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.message.OutboundMessage;

import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class SocketHandler {

    private final InboundHandler inboundHandler;
    private final OutboundHandler outboundHandler;

    public SocketHandler(ExecutorService executorService, Socket socket, IInboundMessageReader inboundMessageReader, IErrorMessageReader errorMessageReader) {
        inboundHandler = new InboundHandler(socket, inboundMessageReader);
        outboundHandler = new OutboundHandler(socket, errorMessageReader);

        executorService.submit(inboundHandler);
        executorService.submit(outboundHandler);
    }

    public Optional<String> getPlayer() {
        return inboundHandler.getPlayer();
    }

    public void shutdown() {
        inboundHandler.shutdown();
        outboundHandler.shutdown();
    }

    public void schedulePacket(OutboundMessage message) {
        outboundHandler.schedulePacket(message);
    }

}
