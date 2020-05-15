package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.server.message.ErrorMessage;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

public class OutboundHandler implements Runnable {

    /**
     * The output stream, for sending messages
     */
    private final OutputStream outputStream;

    /**
     * The error message consumer, handling errors
     */
    private final Consumer<ErrorMessage> errorMessageConsumer;

    /**
     * The packets to be sent
     */
    private final BlockingDeque<OutboundMessage> pendingPackets = new LinkedBlockingDeque<>();

    /**
     * Whether or not the handler should be running
     */
    private boolean active = true;

    public OutboundHandler(OutputStream outputStream, Consumer<ErrorMessage> errorMessageConsumer) {
        this.outputStream = outputStream;
        this.errorMessageConsumer = errorMessageConsumer;
    }

    @Override
    public void run() {
        while (active) {
            OutboundMessage message;
            try {
                message = pendingPackets.take();
            } catch (InterruptedException exception) {
                // Server is shutting down
                continue;
            }

            PrintWriter out = new PrintWriter(outputStream);
            out.write(message.getMessage() + System.lineSeparator());

            if (out.checkError()) {
                errorMessageConsumer.accept(new ErrorMessage(ErrorMessage.ErrorType.OUTBOUND_MESSAGE_FAILED, message));
            }
        }
    }

    /**
     * Shuts down the handler
     */
    public void shutdown() {
        active = false;
    }

    /**
     * @see IServer#send(OutboundMessage)
     */
    public void schedulePacket(OutboundMessage message) {
        pendingPackets.addLast(message);
    }

}
