package it.polimi.ingsw.server;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.logging.reader.ConsoleLogReader;
import it.polimi.ingsw.common.logging.reader.FileLogReader;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.socket.SocketServer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private static final int INIT_TIMEOUT = 10000;

    private static final String COMMAND_STOP = "stop";

    /**
     * The server implementation instance
     */
    private final IServer server;

    /**
     * The controller
     */
    private final Controller controller;

    public ServerMain(String... args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Logger logger = Logger.getInstance();
        logger.addReader(new ConsoleLogReader(System.out));
        logger.info("Initializing server...");
        logger.filter("\"RequestPlayerPingEvent\"");
        logger.filter("\"PlayerPingEvent\"");

        // TODO: Configuration loading
        // Read first from optional command line args, then from file, then from constants
        ServerConfiguration configuration = ServerConfiguration.fromFile(Path.of(""));
        logger.addReader(new FileLogReader(Path.of(configuration.getLogPath())));
        logger.info("Configuration loaded");
        logger.start(executorService);

        SocketServer server;

        try {
            server = new SocketServer(executorService, configuration.getPort());
        } catch (IllegalArgumentException | IOException exception) {
            logger.exception(exception);
            logger.severe("Unable to start the server, shutting down");

            this.server = null;
            this.controller = null;
            logger.shutdown();
            executorService.shutdownNow();
            return;
        }

        logger.info("Server started");
        executorService.submit(this::runInputListener);

        this.server = server;
        this.controller = new Controller(server);
    }

    private void runInputListener() {
        Logger logger = Logger.getInstance();
        logger.debug("Listening started");

        Scanner scanner = new Scanner(System.in);

        try {
            while (scanner.hasNext()) {
                String input = scanner.next();

                if (input.equals(COMMAND_STOP)) {
                    shutdown();
                    return;
                }

                logger.info("Unknown command");
            }
        } catch (IllegalStateException ignored) {
            // Server is shutting down
        }

        shutdown();
    }

    private void shutdown() {
        Logger logger = Logger.getInstance();
        logger.info("Shutting down, goodbye!");
        controller.shutdown();
        server.shutdown();
        logger.shutdown();
    }

}
