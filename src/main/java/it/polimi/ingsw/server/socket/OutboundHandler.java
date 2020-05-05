package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.message.ErrorMessage;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class OutboundHandler implements Runnable {

    private final Socket socket;

    private final IErrorMessageReader reader;

    private final Deque<OutboundMessage> pendingPackets = new LinkedBlockingDeque<>();

    private boolean active = true;

    public OutboundHandler(Socket socket, IErrorMessageReader reader) {
        this.socket = socket;
        this.reader = reader;
    }

    @Override
    public void run() {
        while (active) {
            while (pendingPackets.size() > 0) {
                OutboundMessage message = pendingPackets.poll();

                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    out.write(message.getMessage());
                    out.flush();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    reader.scheduleRead(new ErrorMessage(ErrorMessage.ErrorType.OUTBOUND_MESSAGE_FAILED, message));
                }
            }
        }
    }

    public void shutdown() {
        active = false;
    }

    public void schedulePacket(OutboundMessage message) {
        pendingPackets.addLast(message);
    }

}
