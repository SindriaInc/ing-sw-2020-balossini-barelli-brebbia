package it.polimi.ingsw.common.io;

import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * An InboundHandler scans for messages from an InputStream and passes them to a message consumer when they are ready,
 * always using a single thread
 */
public class InboundHandler implements Runnable {

    /**
     * The input stream, for reading incoming messages
     */
    private final InputStream inputStream;

    /**
     * The message consumer, handling reads
     */
    private final Consumer<String> inboundMessageConsumer;

    /**
     * Whether or not the handler should be running
     */
    private boolean active = true;

    /**
     * Class constructor
     *
     * @param inputStream The input stream
     * @param inboundMessageConsumer The inbound message handler
     */
    public InboundHandler(InputStream inputStream, Consumer<String> inboundMessageConsumer) {
        this.inputStream = inputStream;
        this.inboundMessageConsumer = inboundMessageConsumer;
    }

    /**
     * Scans for messages in the input stream
     */
    @Override
    public void run() {
        try {
            Scanner in = new Scanner(inputStream);

            while (active && in.hasNextLine()) {
                String packet = in.nextLine();

                inboundMessageConsumer.accept(packet);
            }
        } catch (IllegalStateException ignored) {
            // Stream is closed
        }
    }

    /**
     * Shuts down the handler, stopping any active scan
     */
    public void shutdown() {
        active = false;
    }

}
