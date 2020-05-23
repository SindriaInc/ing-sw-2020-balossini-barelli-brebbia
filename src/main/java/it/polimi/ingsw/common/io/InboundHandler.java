package it.polimi.ingsw.common.io;

import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;

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

    public InboundHandler(InputStream inputStream, Consumer<String> inboundMessageConsumer) {
        this.inputStream = inputStream;
        this.inboundMessageConsumer = inboundMessageConsumer;
    }

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

    public void shutdown() {
        active = false;
    }

}
