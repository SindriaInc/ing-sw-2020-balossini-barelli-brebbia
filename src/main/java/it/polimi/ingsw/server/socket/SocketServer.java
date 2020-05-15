package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.server.IConnectHandler;
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
import java.util.concurrent.LinkedBlockingDeque;

public class SocketServer implements IServer {

    private static class PendingMessage {

        private final String tempName;
        private final ErrorMessage errorMessage;
        private final InboundMessage inboundMessage;

        public PendingMessage(String tempName) {
            this.tempName = tempName;
            this.errorMessage = null;
            this.inboundMessage = null;
        }

        public PendingMessage(ErrorMessage errorMessage) {
            this.errorMessage = errorMessage;
            this.tempName = null;
            this.inboundMessage = null;
        }

        public PendingMessage(InboundMessage inboundMessage) {
            this.inboundMessage = inboundMessage;
            this.tempName = null;
            this.errorMessage = null;
        }

        public Optional<String> getTempName() {
            return Optional.ofNullable(tempName);
        }

        public Optional<ErrorMessage> getErrorMessage() {
            return Optional.ofNullable(errorMessage);
        }

        public Optional<InboundMessage> getInboundMessage() {
            return Optional.ofNullable(inboundMessage);
        }

    }

    /**
     * The executor service, providing threads for tasks
     */
    private final ExecutorService executorService;

    /**
     * The ServerSocket instance, guaranteed to be open until shutdown
     */
    private final ServerSocket serverSocket;

    /**
     * The list of IConnectHandlers, called when a player connects
     */
    private final List<IConnectHandler> connectHandlers = new ArrayList<>();

    /**
     * The list of IMessageHandler, called when a player sends a packet
     */
    private final List<IMessageHandler> packetHandlers = new ArrayList<>();

    /**
     * The list of IErrorHandler, called when there's an error accepting a socket or sending a message
     */
    private final List<IErrorHandler> errorHandlers = new ArrayList<>();

    /**
     * The list of connection handlers, one for each connected player
     */
    private final List<SocketHandler> socketHandlers = new ArrayList<>();

    /**
     * The list of messages to be sent
     */
    private final BlockingDeque<PendingMessage> pendingMessages = new LinkedBlockingDeque<>();

    /**
     * Whether or not the server is active and not shutting down
     */
    private boolean active;

    /**
     * The value used to determine the next player's fake name
     */
    private int nextPlayerId;

    public SocketServer(ExecutorService executorService, int port) throws IllegalArgumentException, IOException {
        this.executorService = executorService;

        serverSocket = new ServerSocket(port);
        active = true;

        executorService.submit(this::readMessages);
        executorService.submit(this::runSocket);
    }

    @Override
    public void send(OutboundMessage message) {
        Logger.getInstance().debug("Sending outbound message to \"" + message.getDestinationPlayer() + "\": " + message.getMessage());

        for (SocketHandler socketHandler : socketHandlers) {
            if (!socketHandler.getPlayer().equals(message.getDestinationPlayer())) {
                continue;
            }

            socketHandler.schedulePacket(message);
        }
    }

    @Override
    public void registerHandler(IConnectHandler connectHandler) {
        connectHandlers.add(connectHandler);
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
        Optional<SocketHandler> optionalSocketHandler = socketHandlers.stream()
                .filter(socketHandler -> socketHandler.getPlayer().equals(player))
                .findFirst();

        if (optionalSocketHandler.isEmpty()) {
            return;
        }

        SocketHandler socketHandler = optionalSocketHandler.get();
        socketHandler.shutdown();
        socketHandlers.remove(socketHandler);
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

                Optional<String> tempName = pendingMessage.getTempName();

                if (tempName.isPresent()) {
                    for (IConnectHandler connectHandler : connectHandlers) {
                        connectHandler.onConnect(tempName.get());
                    }
                }

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
        while (active) {
            try {
                Socket socket = serverSocket.accept();
                Logger.getInstance().debug("Accepted a new socket from " + socket.getInetAddress().getHostAddress());

                String tempName = IServer.UNIDENTIFIED_PLAYER_PREFIX + nextPlayerId++;

                SocketHandler handler = new SocketHandler(tempName, executorService, socket, this::scheduleRead, this::scheduleError);
                socketHandlers.add(handler);
                scheduleConnect(tempName);
            } catch (SocketException exception) {
                if (!active) {
                    // Server is shutting down
                    continue;
                }

                scheduleError(new ErrorMessage(ErrorMessage.ErrorType.ACCEPT_FAILED, null));
                Logger.getInstance().exception(exception);
            } catch (IllegalArgumentException | IOException exception) {
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

    private void scheduleConnect(String tempName) {
        pendingMessages.addLast(new PendingMessage(tempName));
    }

    private void scheduleError(ErrorMessage message) {
        pendingMessages.addLast(new PendingMessage(message));
    }

    private void scheduleRead(InboundMessage message) {
        pendingMessages.addLast(new PendingMessage(message));
    }

}
