package it.polimi.ingsw.common.socket;

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
    private final Consumer<String> errorMessageConsumer;

    /**
     * The packets to be sent
     */
    private final BlockingDeque<String> pendingPackets = new LinkedBlockingDeque<>();

    /**
     * Whether or not the handler should be running
     */
    private boolean active = true;

    public OutboundHandler(OutputStream outputStream, Consumer<String> errorMessageConsumer) {
        this.outputStream = outputStream;
        this.errorMessageConsumer = errorMessageConsumer;
    }

    @Override
    public void run() {
        while (active) {
            String message;
            try {
                message = pendingPackets.take();
            } catch (InterruptedException exception) {
                // Application is shutting down
                continue;
            }

            PrintWriter out = new PrintWriter(outputStream);
            out.write(message + System.lineSeparator());

            if (out.checkError()) {
                errorMessageConsumer.accept(message);
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
     * Schedules the packet to be sent later to the other entity
     */
    public void schedulePacket(String message) {
        pendingPackets.addLast(message);
    }

}
