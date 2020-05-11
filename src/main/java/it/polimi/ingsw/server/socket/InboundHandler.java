package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.server.message.InboundMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;

public class InboundHandler implements Runnable {

    private final Socket socket;

    private final IInboundMessageReader reader;

    private boolean active = true;

    private String player;

    public InboundHandler(Socket socket, IInboundMessageReader reader) {
        this.socket = socket;
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());

            while (active) {
                String packet = in.nextLine();

                if (player == null) {
                    reader.scheduleRead(new InboundMessage(packet, player -> this.player = player));
                } else {
                    reader.scheduleRead(new InboundMessage(player, packet));
                }

                Logger.getInstance().debug("Received message: " + packet);
            }
        } catch (IOException exception) {
            Logger.getInstance().exception(exception);
        }
    }

    public void shutdown() {
        active = false;
    }

    public Optional<String> getPlayer() {
        return Optional.of(player);
    }

}
