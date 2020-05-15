package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.server.message.InboundMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class InboundHandler implements Runnable {

    private final Socket socket;

    private final IInboundMessageReader reader;

    private boolean active = true;

    private String player;

    public InboundHandler(String tempName, Socket socket, IInboundMessageReader reader) {
        this.socket = socket;
        this.reader = reader;
        this.player = tempName;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());

            while (active && in.hasNextLine()) {
                String packet = in.nextLine();

                if (!IServer.isIdentified(player)) {
                    reader.scheduleRead(new InboundMessage(packet, player, this::onLogin));
                } else {
                    reader.scheduleRead(new InboundMessage(packet, player));
                }

                Logger.getInstance().debug("Received message: " + packet);
            }
        } catch (IOException ignored) {
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
