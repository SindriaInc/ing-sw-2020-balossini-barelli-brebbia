package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.common.event.PlayerLogoutEvent;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.serializer.GsonEventSerializer;
import it.polimi.ingsw.server.message.InboundMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;

public class InboundHandler implements Runnable {

    private final Socket socket;

    private final IInboundMessageReader reader;

    private final Runnable onLoginFail;

    private boolean active = true;

    private String player;

    public InboundHandler(Socket socket, IInboundMessageReader reader, Runnable onLoginFail) {
        this.socket = socket;
        this.reader = reader;
        this.onLoginFail = onLoginFail;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());

            while (active) {
                String packet = in.nextLine();

                if (player == null) {
                    reader.scheduleRead(new InboundMessage(packet, this::onLogin, onLoginFail));
                } else {
                    reader.scheduleRead(new InboundMessage(player, packet));
                }

                Logger.getInstance().debug("Received message: " + packet);
            }
        } catch (IOException exception) {
            // Socket is closed
            reader.scheduleRead(new InboundMessage(player, new GsonEventSerializer().serialize(new PlayerLogoutEvent(player))));
        }
    }

    public void shutdown() {
        active = false;
    }

    public Optional<String> getPlayer() {
        return Optional.ofNullable(player);
    }

    private void onLogin(String player) {
        this.player = player;
    }

}
