package it.polimi.ingsw.server.socket;

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
        System.out.println("Helo");

        try {
            Scanner in = new Scanner(socket.getInputStream());

            while (active) {
                while (player == null) {
                    String packet = in.nextLine();
                    player = packet;
                    System.out.println("Helo: " + packet);
                }

                String packet = in.nextLine();
                reader.scheduleRead(new InboundMessage(player, packet));
                System.out.println("Hulu: " + packet);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void shutdown() {
        active = false;
    }

    public Optional<String> getPlayer() {
        return Optional.of(player);
    }

}
