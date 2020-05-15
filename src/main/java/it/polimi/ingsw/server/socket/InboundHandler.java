package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.server.message.InboundMessage;

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
    private final Consumer<InboundMessage> inboundMessageConsumer;

    /**
     * Whether or not the handler should be running
     */
    private boolean active = true;

    /**
     * The player associated to this handler, or the fake temporary name if not yet logged on
     */
    private String player;

    public InboundHandler(InputStream inputStream, String tempName, Consumer<InboundMessage> inboundMessageConsumer) {
        this.inputStream = inputStream;
        this.inboundMessageConsumer = inboundMessageConsumer;
        this.player = tempName;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(inputStream);

            while (active && in.hasNextLine()) {
                String packet = in.nextLine();

                if (!IServer.isIdentified(player)) {
                    inboundMessageConsumer.accept(new InboundMessage(packet, player, this::onLogin));
                } else {
                    inboundMessageConsumer.accept(new InboundMessage(packet, player));
                }

                Logger.getInstance().debug("Received message: " + packet);
            }
        } catch (IllegalStateException ignored) {
            // Socket is closed
        }
    }

    public void shutdown() {
        active = false;
    }

    public String getPlayer() {
        return player;
    }

    private void onLogin(String player) {
        this.player = player;
    }

}
