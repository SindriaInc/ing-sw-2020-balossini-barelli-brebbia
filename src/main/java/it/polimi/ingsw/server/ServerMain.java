package it.polimi.ingsw.server;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.logging.reader.ConsoleLogReader;
import it.polimi.ingsw.common.logging.reader.FileLogReader;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.socket.SocketServer;

import java.nio.file.Path;
import java.util.Scanner;

public class ServerMain {

    private static final String COMMAND_STOP = "stop";

    /**
     * The server implementation instance
     */
    private final IServer server;

    public ServerMain(String... args) {
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

        server = new SocketServer(configuration.getPort());
        logger.info("Server started");
        logger.start();

        // TODO: The initialization below serves as an example and will be replaced
        new Controller(server);

        listenForInput();

        logger.debug("Main shutdown");
    }

    private void listenForInput() {
        Logger logger = Logger.getInstance();
        logger.debug("Listening started");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String input = scanner.next();

            if (input.equals(COMMAND_STOP)) {
                logger.info("Shutting down, goodbye!");
                server.shutdown();
                logger.shutdown();
                break;
            }

            logger.info("Unknown command");
        }
    }

}
