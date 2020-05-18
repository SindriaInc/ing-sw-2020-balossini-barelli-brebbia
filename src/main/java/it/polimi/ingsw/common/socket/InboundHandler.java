package it.polimi.ingsw.common.socket;

import it.polimi.ingsw.common.logging.Logger;

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
    private final Consumer<String> inboundPacketConsumer;

    /**
     * Whether or not the handler should be running
     */
    private boolean active = true;

    public InboundHandler(InputStream inputStream, Consumer<String> inboundPacketConsumer) {
        this.inputStream = inputStream;
        this.inboundPacketConsumer = inboundPacketConsumer;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(inputStream);

            while (active && in.hasNextLine()) {
                String packet = in.nextLine();

                inboundPacketConsumer.accept(packet);

                Logger.getInstance().debug("Received message: " + packet);
            }
        } catch (IllegalStateException ignored) {
            // Socket is closed
        }
    }

    public void shutdown() {
        active = false;
    }

}
