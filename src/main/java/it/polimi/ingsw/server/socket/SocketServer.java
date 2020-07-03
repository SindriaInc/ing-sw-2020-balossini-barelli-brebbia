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

/**
 * A socket implementation of <code>IServer</code>
 *
 * Players will connect to the server by instancing a TCP connection
 * The server can receive and send packets simultaneously to multiple clients
 *
 * Connections are managed by worker threads provided by an <code>ExecutorService</code>
 * Each thread will only be used when effectively needed and will never busy-wait
 */
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

    /**
     * Class constructor, submits tasks to the executor service
     *
     * @param executorService The executor service
     * @param port The port which the server will be bound to
     * @throws IllegalArgumentException if the port is not valid
     * @throws IOException if there's an error while setting up the server
     */
    public SocketServer(ExecutorService executorService, int port) throws IllegalArgumentException, IOException {
        this.executorService = executorService;

        serverSocket = new ServerSocket(port);
        active = true;

        executorService.submit(this::readMessages);
        executorService.submit(this::runSocket);
    }

    /**
     * @see IServer#send(OutboundMessage)
     */
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

    /**
     * @see IServer#registerHandler(IConnectHandler)
     */
    @Override
    public void registerHandler(IConnectHandler connectHandler) {
        connectHandlers.add(connectHandler);
    }

    /**
     * @see IServer#registerHandler(IMessageHandler)
     */
    @Override
    public void registerHandler(IMessageHandler packetHandler) {
        packetHandlers.add(packetHandler);
    }

    /**
     * @see IServer#registerHandler(IErrorHandler)
     */
    @Override
    public void registerHandler(IErrorHandler errorHandler) {
        errorHandlers.add(errorHandler);
    }

    /**
     * @see IServer#identify(String, String)
     */
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

    /**
     * @see IServer#disconnect(String)
     */
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

    /**
     * @see IServer#shutdown()
     */
    @Override
    public void shutdown() {
        active = false;

        try {
            serverSocket.close();
        } catch (IOException exception) {
            Logger.getInstance().exception(exception);
        }
    }

    /**
     * Reads incoming messages in a single thread and executes the associated runnable
     *
     * The method will end its execution after the server is shut down
     */
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

    /**
     * Runs the server socket
     *
     * The socket will accept new connections and create an appropriate <code>SocketHandler</code> when needed
     *
     * The method will end its execution after the server is shut down
     */
    private void runSocket() {
        while (active) {
            try {
                Socket socket = serverSocket.accept();
                Logger.getInstance().debug("Accepted a new socket from " + socket.getInetAddress().getHostAddress());
                // Avoid the system delaying packets
                socket.setTcpNoDelay(true);

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

    /**
     * Schedules a connect message to be read later
     *
     * @param tempName The player temporary name
     */
    private void scheduleConnect(String tempName) {
        pendingMessages.addLast(() -> {
            for (IConnectHandler connectHandler : connectHandlers) {
                connectHandler.onConnect(tempName);
            }
        });
    }

    /**
     * Schedules an error message to be read later
     *
     * @param message The error message
     */
    private void scheduleError(ErrorMessage message) {
        pendingMessages.addLast(() -> {
            for (IErrorHandler errorHandler : errorHandlers) {
                errorHandler.onError(message);
            }
        });
    }

    /**
     * Schedules an error message received by a <code>SocketHandler</code> to be read later
     *
     * @param socketHandler The socket handler
     * @param message The error message
     */
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

    /**
     * Schedules a normal message received by the socket to be read later
     *
     * @param socketHandler The socket handler
     * @param message The message
     */
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

    /**
     * Obtains the player associated with the <code>SocketHandler</code>
     *
     * @param socketHandler The socket handler
     * @return The player, or <code>Optional.empty()</code> if no player is associated with the handler
     */
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
