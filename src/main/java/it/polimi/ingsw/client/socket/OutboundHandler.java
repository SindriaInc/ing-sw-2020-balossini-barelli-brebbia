package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.client.message.ErrorMessage;
import it.polimi.ingsw.common.logging.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class OutboundHandler implements Runnable {

    private final Socket socket;

    private final IErrorMessageReader reader;

    private final BlockingDeque<String> pendingPackets = new LinkedBlockingDeque<>();

    private boolean active = true;

    public OutboundHandler(Socket socket, IErrorMessageReader reader) {
        this.socket = socket;
        this.reader = reader;
    }

    @Override
    public void run() {
        while (active) {
            String message;

            try {
                message = pendingPackets.take();
            } catch (InterruptedException exception) {
                // Shutting down
                continue;
            }

            Logger.getInstance().debug("Sending packet: " + message);

            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.write(message + System.lineSeparator());
                out.flush();
            } catch (IOException exception) {
                Logger.getInstance().exception(exception);
                reader.scheduleRead(new ErrorMessage(ErrorMessage.ErrorType.MESSAGE_FAILED, message));
            }
        }
    }

    public void shutdown() {
        active = false;
    }

    public void schedulePacket(String message) {
        pendingPackets.addLast(message);
    }

}
