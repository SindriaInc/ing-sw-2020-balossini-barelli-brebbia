package it.polimi.ingsw.common.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

/**
 * An OutboundHandler takes messages in a thread-safe way and prints them to the <code>OutputStream</code> using a
 * single thread
 */
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
     * The messages to be sent
     */
    private final BlockingDeque<String> pendingMessages = new LinkedBlockingDeque<>();

    /**
     * Whether or not the handler should be running
     */
    private boolean active = true;

    /**
     * Class constructor
     *
     * @param outputStream The output stream
     * @param errorMessageConsumer The error messages handler
     */
    public OutboundHandler(OutputStream outputStream, Consumer<String> errorMessageConsumer) {
        this.outputStream = outputStream;
        this.errorMessageConsumer = errorMessageConsumer;
    }

    /**
     * Takes messages to be sent and prints them
     */
    @Override
    public void run() {
        while (active) {
            String message;
            try {
                message = pendingMessages.take();
            } catch (InterruptedException exception) {
                // Application is shutting down
                continue;
            }

            print(message);
        }
    }

    /**
     * Shuts down the handler
     */
    public void shutdown() {
        active = false;
    }

    /**
     * Schedules the message to be sent later
     * @param message The message
     */
    public void scheduleMessage(String message) {
        pendingMessages.addLast(message);
    }

    /**
     * Prints the message using the output stream
     * @param message The message
     */
    protected void print(String message) {
        PrintWriter out = new PrintWriter(outputStream);
        out.write(message + System.lineSeparator());
        out.flush();

        if (out.checkError()) {
            errorMessageConsumer.accept(message);
        }
    }

}
