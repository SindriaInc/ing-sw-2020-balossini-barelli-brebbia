package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.common.logging.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class InboundHandler implements Runnable {

    private final Socket socket;

    private final IInboundMessageReader reader;

    private boolean active = true;

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
                reader.scheduleRead(packet);
                Logger.getInstance().debug("Received message: " + packet);
            }
        } catch (IOException exception) {
            // Socket is closing
        }
    }

    public void shutdown() {
        active = false;
    }

}
