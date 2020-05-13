package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.common.event.response.ResponseInvalidParametersEvent;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.serializer.GsonEventSerializer;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class SocketHandler {

    private final Socket socket;

    private final InboundHandler inboundHandler;
    private final OutboundHandler outboundHandler;

    public SocketHandler(ExecutorService executorService, Socket socket, IInboundMessageReader inboundMessageReader, IErrorMessageReader errorMessageReader) {
        this.socket = socket;

        inboundHandler = new InboundHandler(socket, inboundMessageReader, this::onLoginFail);
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

        try {
            socket.close();
        } catch (IOException exception) {
            Logger.getInstance().warning("Exception while closing socket");
            Logger.getInstance().exception(exception);
        }
    }

    public void schedulePacket(OutboundMessage message) {
        outboundHandler.schedulePacket(message);
    }

    private void onLoginFail() {
        // Need to send the packet back to the player without having an association in the socket
        schedulePacket(new OutboundMessage(null, new GsonEventSerializer().serialize(new ResponseInvalidParametersEvent(null))));
    }

}
