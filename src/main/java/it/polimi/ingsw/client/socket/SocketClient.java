package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.client.IClient;
import it.polimi.ingsw.client.IErrorHandler;
import it.polimi.ingsw.client.IMessageHandler;
import it.polimi.ingsw.client.message.ErrorMessage;
import it.polimi.ingsw.common.logging.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class SocketClient implements IClient {

    private static class PendingMessage {

        private final ErrorMessage errorMessage;
        private final String inboundMessage;

        public PendingMessage(ErrorMessage errorMessage) {
            this.errorMessage = errorMessage;
            this.inboundMessage = null;
        }

        public PendingMessage(String inboundMessage) {
            this.inboundMessage = inboundMessage;
            this.errorMessage = null;
        }

        public Optional<ErrorMessage> getErrorMessage() {
            return Optional.ofNullable(errorMessage);
        }

        public Optional<String> getInboundMessage() {
            return Optional.ofNullable(inboundMessage);
        }

    }

    private static final int CONNECT_TIMEOUT = 3000;

    private final List<IMessageHandler> packetHandlers = new ArrayList<>();

    private final List<IErrorHandler> errorHandlers = new ArrayList<>();

    private final BlockingDeque<PendingMessage> pendingMessages = new LinkedBlockingDeque<>();

    private final ExecutorService executorService;

    private final String ip;

    private final int port;

    private Socket socket;

    private boolean active;

    private InboundHandler inboundHandler;

    private OutboundHandler outboundHandler;

    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;

        active = true;

        executorService = Executors.newCachedThreadPool();
        executorService.submit(this::readMessages);
        executorService.submit(this::runSocket);
    }

    @Override
    public void send(String message) {
        outboundHandler.schedulePacket(message);
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

        if (inboundHandler != null) {
            inboundHandler.shutdown();
        }

        if (outboundHandler != null) {
            outboundHandler.shutdown();
        }

        executorService.shutdownNow();

        try {
            socket.close();
        } catch (IOException exception) {
            Logger.getInstance().warning("Exception while closing socket");
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

                Optional<String> inboundMessage = pendingMessage.getInboundMessage();

                if (inboundMessage.isPresent()) {
                    for (IMessageHandler packetHandler : packetHandlers) {
                        packetHandler.onMessage(inboundMessage.get());
                    }
                }
            } catch (InterruptedException ignored) {
                // Client is shutting down
            }
        }
    }

    private void runSocket() {
        socket = new Socket();

        try {
            Logger.getInstance().debug("Trying to connect to " + ip + ":" + port);
            socket.connect(new InetSocketAddress(ip, port), CONNECT_TIMEOUT);
            Logger.getInstance().debug("Connected successfully");
        } catch (IOException exception) {
            scheduleError(new ErrorMessage(ErrorMessage.ErrorType.INIT_FAILED, null));
            Logger.getInstance().exception(exception);
            return;
        }

        inboundHandler = new InboundHandler(socket, this::scheduleRead);
        outboundHandler = new OutboundHandler(socket, this::scheduleError);
        executorService.submit(inboundHandler);
        executorService.submit(outboundHandler);

        scheduleRead(IClient.CONNECT_MESSAGE);
    }

    private void scheduleRead(String message) {
        pendingMessages.addLast(new PendingMessage(message));
    }

    private void scheduleError(ErrorMessage message) {
        pendingMessages.addLast(new PendingMessage(message));
    }

}
