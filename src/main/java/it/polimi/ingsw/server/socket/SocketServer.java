package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.socket.SocketHandler;
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
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

public class SocketServer implements IServer {

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
     * The connection handlers, one for each connected player
     * The key of the map is the name of the player (or the temporary fake name if not logged on)
     */
    private final Map<String, SocketHandler> socketHandlers = new HashMap<>();

    /**
     * The list of messages to be sent
     */
    private final BlockingDeque<Runnable> pendingMessages = new LinkedBlockingDeque<>();

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

        for (Map.Entry<String, SocketHandler> entry : socketHandlers.entrySet()) {
            String player = entry.getKey();
            SocketHandler socketHandler = entry.getValue();

            if (!player.equals(message.getDestinationPlayer())) {
                continue;
            }

            socketHandler.schedulePacket(message.getMessage());
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
    public void identify(String tempName, String player) {
        SocketHandler socketHandler = socketHandlers.get(tempName);

        if (socketHandler == null) {
            Logger.getInstance().severe("Tried to identify an invalid player: " + tempName);
            return;
        }

        socketHandlers.put(player, socketHandler);
        socketHandlers.remove(tempName);
    }

    @Override
    public void disconnect(String player) {
        SocketHandler socketHandler = socketHandlers.get(player);

        if (socketHandler == null) {
            Logger.getInstance().debug("Tried disconnecting an invalid player: " + player);
            return;
        }

        socketHandler.shutdown();
        socketHandlers.remove(player);

        String playerInfo;

        if (IServer.isIdentified(player)) {
            playerInfo = " (Identified as " + player + ")";
        } else {
            playerInfo = " (Temporarily known as " + player + ")";
        }

        Logger.getInstance().debug("Disconnected " + socketHandler.getAddress() + playerInfo);
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
                Runnable pendingMessage = pendingMessages.take();
                pendingMessage.run();
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

                SocketHandler handler = new SocketHandler(executorService, socket, this::scheduleRead, this::scheduleError);
                socketHandlers.put(tempName, handler);
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

        for (SocketHandler handler : socketHandlers.values()) {
            handler.shutdown();
        }
        socketHandlers.clear();

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
        pendingMessages.addLast(() -> {
            for (IConnectHandler connectHandler : connectHandlers) {
                connectHandler.onConnect(tempName);
            }
        });
    }

    private void scheduleError(ErrorMessage message) {
        pendingMessages.addLast(() -> {
            for (IErrorHandler errorHandler : errorHandlers) {
                errorHandler.onError(message);
            }
        });
    }

    private void scheduleError(SocketHandler socketHandler, String message) {
        Optional<String> player = getPlayerByHandler(socketHandler);

        if (player.isEmpty()) {
            Logger.getInstance().severe("Received an error from an invalid socket handler");
            return;
        }

        OutboundMessage outboundMessage = new OutboundMessage(player.get(), message);
        ErrorMessage errorMessage = new ErrorMessage(ErrorMessage.ErrorType.OUTBOUND_MESSAGE_FAILED, outboundMessage);

        scheduleError(errorMessage);
    }

    private void scheduleRead(SocketHandler socketHandler, String message) {
        Optional<String> player = getPlayerByHandler(socketHandler);

        if (player.isEmpty()) {
            Logger.getInstance().severe("Received a packet from an invalid socket handler");
            return;
        }

        InboundMessage inboundMessage = new InboundMessage(player.get(), message);

        pendingMessages.addLast(() -> {
            for (IMessageHandler packetHandler : packetHandlers) {
                packetHandler.onMessage(inboundMessage);
            }
        });
    }

    private Optional<String> getPlayerByHandler(SocketHandler socketHandler) {
        for (Map.Entry<String, SocketHandler> entry : socketHandlers.entrySet()) {
            if (entry.getValue() != socketHandler) {
                continue;
            }

            return Optional.of(entry.getKey());
        }

        return Optional.empty();
    }

}
