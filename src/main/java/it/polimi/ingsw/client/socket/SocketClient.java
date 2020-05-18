package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.client.IClient;
import it.polimi.ingsw.client.IErrorHandler;
import it.polimi.ingsw.client.IMessageHandler;
import it.polimi.ingsw.client.message.ErrorMessage;
import it.polimi.ingsw.common.socket.SocketHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class SocketClient implements IClient {

    private static final int CONNECT_TIMEOUT = 3000;

    /**
     * The executor service, providing threads for tasks
     */
    private final ExecutorService executorService;

    /**
     * The list of IMessageHandler, called when the server sends a packet
     */
    private final List<IMessageHandler> packetHandlers = new ArrayList<>();

    /**
     * The list of IErrorHandler, called when there's an error accepting a socket or sending a message
     */
    private final List<IErrorHandler> errorHandlers = new ArrayList<>();

    /**
     * The list of messages to be sent
     */
    private final BlockingDeque<Runnable> pendingMessages = new LinkedBlockingDeque<>();

    /**
     * The socket handler, which allows to send and receive messages
     */
    private final SocketHandler socketHandler;

    /**
     * Whether or not the client is active and not shutting down
     */
    private boolean active;

    public SocketClient(String ip, int port) throws IllegalArgumentException, IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port), CONNECT_TIMEOUT);

        active = true;

        executorService = Executors.newCachedThreadPool();
        executorService.submit(this::readMessages);

        socketHandler = new SocketHandler(executorService, socket, this::onMessage, this::onError);
    }

    @Override
    public void send(String message) {
        socketHandler.schedulePacket(message);
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

        socketHandler.shutdown();
        executorService.shutdownNow();
    }

    private void readMessages() {
        while (active) {
            try {
                Runnable pendingMessage = pendingMessages.take();
                pendingMessage.run();
            } catch (InterruptedException ignored) {
                // Client is shutting down
            }
        }
    }

    private void onMessage(SocketHandler socketHandler, String message) {
        pendingMessages.addLast(() -> {
            for (IMessageHandler packetHandler : packetHandlers) {
                packetHandler.onMessage(message);
            }
        });
    }

    private void onError(SocketHandler socketHandler, String message) {
        ErrorMessage errorMessage = new ErrorMessage(ErrorMessage.ErrorType.MESSAGE_FAILED, message);

        pendingMessages.addLast(() -> {
            for (IErrorHandler errorHandler : errorHandlers) {
                errorHandler.onError(errorMessage);
            }
        });
    }

}
