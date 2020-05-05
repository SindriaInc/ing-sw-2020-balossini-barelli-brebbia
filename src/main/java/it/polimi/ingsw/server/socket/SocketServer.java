package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.*;
import it.polimi.ingsw.server.message.ErrorMessage;
import it.polimi.ingsw.server.message.InboundMessage;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class SocketServer implements IServer {

    private final int port;

    private final List<IMessageHandler> packetHandlers = new ArrayList<>();

    private final List<IErrorHandler> errorHandlers = new ArrayList<>();

    private final List<SocketHandler> socketHandlers = new ArrayList<>();

    private final Deque<ErrorMessage> pendingErrors = new LinkedBlockingDeque<>();

    private final Deque<InboundMessage> pendingReads = new LinkedBlockingDeque<>();

    private boolean active;

    public SocketServer(int port) {
        this.port = port;

        active = true;
        new Thread(this::runSocket).start();
    }

    @Override
    public void send(OutboundMessage message) {
        // TODO: Replace with logging
        System.out.println("Sending outbound message: " + message.getMessage());

        for (SocketHandler socketHandler : socketHandlers) {
            if (socketHandler.getPlayer().isEmpty()) {
                continue;
            }

            if (!socketHandler.getPlayer().get().equals(message.getDestinationPlayer())) {
                continue;
            }

            socketHandler.schedulePacket(message);
        }
    }

    @Override
    public void registerHandler(IMessageHandler packetHandler) {
        packetHandlers.add(packetHandler);
    }

    @Override
    public void registerHandler(IErrorHandler errorHandler) {
        errorHandlers.add(errorHandler);
    }

    @Override
    public void shutdown() {
        active = false;
    }

    @Override
    public void tick() {
        while (pendingErrors.size() > 0) {
            ErrorMessage message = pendingErrors.poll();

            for (IErrorHandler errorHandler : errorHandlers) {
                errorHandler.onError(message);
            }
        }

        while (pendingReads.size() > 0) {
            InboundMessage message = pendingReads.poll();

            for (IMessageHandler packetHandler : packetHandlers) {
                packetHandler.onMessage(message);
            }
        }
    }

    private void runSocket() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException exception) {
            scheduleError(new ErrorMessage(ErrorMessage.ErrorType.INIT_FAILED, null));
            System.err.println(exception.getMessage());
            return;
        }

        while (active) {
            try {
                Socket socket = serverSocket.accept();
                SocketHandler handler = new SocketHandler(executorService, socket, this::scheduleRead, this::scheduleError);
                socketHandlers.add(handler);
            } catch (IOException exception) {
                scheduleError(new ErrorMessage(ErrorMessage.ErrorType.ACCEPT_FAILED, null));
                System.err.println(exception.getMessage());
            }
        }

        for (SocketHandler socketHandler : socketHandlers) {
            socketHandler.shutdown();
        }
        executorService.shutdown();
    }

    private void scheduleError(ErrorMessage message) {
        pendingErrors.addLast(message);
    }

    private void scheduleRead(InboundMessage message) {
        pendingReads.addLast(message);
    }

}
