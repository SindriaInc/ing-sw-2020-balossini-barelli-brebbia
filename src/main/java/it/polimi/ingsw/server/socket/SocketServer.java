package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.server.IErrorHandler;
import it.polimi.ingsw.server.IMessageHandler;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.server.message.ErrorMessage;
import it.polimi.ingsw.server.message.InboundMessage;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class SocketServer implements IServer {

    private static class PendingMessage {

        private final ErrorMessage errorMessage;
        private final InboundMessage inboundMessage;

        public PendingMessage(ErrorMessage errorMessage) {
            this.errorMessage = errorMessage;
            this.inboundMessage = null;
        }

        public PendingMessage(InboundMessage inboundMessage) {
            this.inboundMessage = inboundMessage;
            this.errorMessage = null;
        }

        public Optional<ErrorMessage> getErrorMessage() {
            return Optional.ofNullable(errorMessage);
        }

        public Optional<InboundMessage> getInboundMessage() {
            return Optional.ofNullable(inboundMessage);
        }

    }

    private final int port;

    private final List<IMessageHandler> packetHandlers = new ArrayList<>();

    private final List<IErrorHandler> errorHandlers = new ArrayList<>();

    private final List<SocketHandler> socketHandlers = new ArrayList<>();

    private final BlockingDeque<PendingMessage> pendingMessages = new LinkedBlockingDeque<>();

    private final ExecutorService executorService;

    private boolean active;

    private ServerSocket serverSocket;

    public SocketServer(int port) {
        this.port = port;

        active = true;

        executorService = Executors.newCachedThreadPool();
        executorService.submit(this::readMessages);
        executorService.submit(this::runSocket);
    }

    @Override
    public void send(OutboundMessage message) {
        Logger.getInstance().debug("Sending outbound message to \"" + message.getDestinationPlayer() + "\": " + message.getMessage());

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
    public void disconnect(String player) {
        for (SocketHandler socketHandler : socketHandlers) {
            if (socketHandler.getPlayer().isEmpty() || !socketHandler.getPlayer().get().equals(player)) {
                continue;
            }

            socketHandler.shutdown();
        }
    }

    @Override
    public void shutdown() {
        active = false;

        try {
            serverSocket.close();
        } catch (IOException exception) {
            Logger.getInstance().exception(exception);
        }
    }

    private void readMessages() {
        while (active) {
            try {
                PendingMessage pendingMessage = pendingMessages.take();

                Optional<ErrorMessage> errorMessage = pendingMessage.getErrorMessage();

                if (errorMessage.isPresent()) {
                    for (IErrorHandler errorHandler : errorHandlers) {
                        errorHandler.onError(errorMessage.get());
                    }
                }

                Optional<InboundMessage> inboundMessage = pendingMessage.getInboundMessage();

                if (inboundMessage.isPresent()) {
                    for (IMessageHandler packetHandler : packetHandlers) {
                        packetHandler.onMessage(inboundMessage.get());
                    }
                }
            } catch (InterruptedException ignored) {
                // Server is shutting down
            }
        }
    }

    private void runSocket() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException exception) {
            scheduleError(new ErrorMessage(ErrorMessage.ErrorType.INIT_FAILED, null));
            Logger.getInstance().exception(exception);
            return;
        }

        while (active) {
            try {
                Socket socket = serverSocket.accept();
                Logger.getInstance().debug("Accepted a new socket from " + socket.getInetAddress().getHostAddress());
                SocketHandler handler = new SocketHandler(executorService, socket, this::scheduleRead, this::scheduleError);
                socketHandlers.add(handler);
            } catch (SocketException exception) {
                if (!active) {
                    // Server is shutting down
                    continue;
                }

                scheduleError(new ErrorMessage(ErrorMessage.ErrorType.ACCEPT_FAILED, null));
                Logger.getInstance().exception(exception);
            } catch (IOException exception) {
                scheduleError(new ErrorMessage(ErrorMessage.ErrorType.ACCEPT_FAILED, null));
                Logger.getInstance().exception(exception);
            }
        }

        for (SocketHandler handler : socketHandlers) {
            handler.shutdown();
        }

        // Interrupt socket handlers
        executorService.shutdownNow();

        try {
            serverSocket.close();
        } catch (IOException exception) {
            Logger.getInstance().exception(exception);
        }

        Logger.getInstance().debug("Socket shutdown");
    }

    private void scheduleError(ErrorMessage message) {
        pendingMessages.addLast(new PendingMessage(message));
    }

    private void scheduleRead(InboundMessage message) {
        pendingMessages.addLast(new PendingMessage(message));
    }

}
