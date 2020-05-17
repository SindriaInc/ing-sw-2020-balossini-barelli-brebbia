package it.polimi.ingsw.server;

import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.logging.reader.ConsoleLogReader;
import it.polimi.ingsw.common.logging.reader.FileLogReader;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.server.socket.SocketServer;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private static final String DEFAULT_CONFIG_PATH = "./config.json";

    private static final String COMMAND_STOP = "stop";

    private static final String ARGUMENT_PORT = "port";
    private static final String ARGUMENT_CONFIG_PATH = "config";
    private static final String ARGUMENT_LOG_PATH = "log-path";
    private static final String ARGUMENT_DECK_PATH = "deck-path";
    private static final String[] ARGUMENTS = {ARGUMENT_PORT, ARGUMENT_CONFIG_PATH, ARGUMENT_LOG_PATH, ARGUMENT_DECK_PATH};

    /**
     * The server implementation instance
     */
    private IServer server;

    /**
     * The controller
     */
    private Controller controller;

    public ServerMain(String... args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Logger logger = Logger.getInstance();
        logger.addReader(new ConsoleLogReader(System.out));
        logger.info("Initializing server...");
        logger.filter("\"RequestPlayerPingEvent\"");
        logger.filter("\"PlayerPingEvent\"");

        // Read the configuration, if it fails shut the server down
        ServerConfiguration configuration;
        try {
            configuration = loadConfiguration(args);
        } catch (IOException exception) {
            dumpAndQuit(exception, exception.getMessage(), executorService);
            return;
        } catch (IllegalArgumentException exception) {
            dumpAndQuit(exception.getMessage(), executorService);
            return;
        }

        // Read the deck, if it fails shut the server down
        Deck deck;
        try {
            deck = configuration.loadDeck();
        } catch (IOException exception) {
            dumpAndQuit(exception, exception.getMessage(), executorService);
            return;
        } catch (ConfigurationException exception) {
            dumpAndQuit(exception.getMessage(), executorService);
            return;
        }

        logger.addReader(new FileLogReader(Path.of(configuration.getLogPath())));
        logger.info("Configuration loaded");
        logger.start(executorService);

        SocketServer server;

        try {
            server = new SocketServer(executorService, configuration.getPort());
        } catch (IllegalArgumentException | IOException exception) {
            dumpAndQuit(exception, "Unable to start the socket server", executorService);
            return;
        }

        logger.info("Server started, listening on port " + configuration.getPort());
        executorService.submit(this::runInputListener);

        this.server = server;
        this.controller = new Controller(server, deck);
    }

    private ServerConfiguration loadConfiguration(String... args) throws IOException, IllegalArgumentException {
        Map<String, String> parameters = readParameters(args);

        ServerConfiguration configuration;

        if (parameters.containsKey("config")) {
            try {
                configuration = ServerConfiguration.readFromFile(parameters.get("config"));
            } catch (IOException exception) {
                throw new IOException("Unable to read the config from the specified path");
            }
        } else if (Files.exists(Path.of(DEFAULT_CONFIG_PATH))) {
            try {
                configuration = ServerConfiguration.readFromFile(DEFAULT_CONFIG_PATH);
            } catch (IOException exception) {
                throw new IOException("Unable to read the config from the default path");
            }
        } else {
            Logger.getInstance().info("Loading default config values");
            configuration = ServerConfiguration.readDefault();
        }

        if (parameters.containsKey("port")) {
            int port;

            try {
                port = Integer.parseInt(parameters.get("port"));
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Invalid port, must be a number");
            }

            configuration = configuration.withPort(port);
        }

        if (parameters.containsKey("log-path")) {
            configuration = configuration.withLogPath(parameters.get("log-path"));
        }

        if (parameters.containsKey("deck-path")) {
            configuration = configuration.withDeckPath(parameters.get("deck-path"));
        }

        return configuration;
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

    private void dumpAndQuit(Exception exception, String message, ExecutorService executorService) {
        Logger.getInstance().exception(exception);
        dumpAndQuit(message, executorService);
    }

    private void dumpAndQuit(String message, ExecutorService executorService) {
        Logger logger = Logger.getInstance();
        logger.severe(message);
        logger.start(executorService);
        logger.shutdown();
        executorService.shutdownNow();
    }

    private void shutdown() {
        Logger logger = Logger.getInstance();
        logger.info("Shutting down, goodbye!");
        controller.shutdown();
        server.shutdown();
        logger.shutdown();
    }

    private Map<String, String> readParameters(String... args) {
        Map<String, String> parameters = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            if (!args[i].startsWith("-")) {
                Logger.getInstance().warning("Invalid parameter name passed: " + args[i]);
                continue;
            }

            String parameter = args[i].substring(1);

            if (Arrays.stream(ARGUMENTS).noneMatch(argument -> argument.equals(parameter))) {
                Logger.getInstance().warning("Unrecognised parameter name passed: " + args[i]);
                continue;
            }

            if (i + 1 >= args.length) {
                Logger.getInstance().warning("No value specified for parameter: " + args[i]);
                continue;
            }

            if (parameters.containsKey(parameter)) {
                Logger.getInstance().warning("Parameter already defined, using the latest value: " + args[i]);
            }

            parameters.put(parameter, args[i + 1]);
            i++;
        }

        return parameters;
    }

}