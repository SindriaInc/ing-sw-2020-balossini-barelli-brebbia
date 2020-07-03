package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CliClientViewer;
import it.polimi.ingsw.client.gui.GuiClientViewer;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.logging.reader.ConsoleLogReader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Main class of the client
 */
public class ClientMain {

    public static final long SLEEP_PERIOD_MS = 100;

    /**
     * Class constructor, initialize the client, use arguments given by <code>ClientApplication</code>
     *
     * @param args The arguments
     */
    public ClientMain(String... args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Logger logger = Logger.getInstance();
        logger.filter("\"RequestPlayerPingEvent\"");
        logger.filter("\"PlayerPingEvent\"");
        logger.info("Initializing client...");

        // Launch the gui by default
        boolean gui = true;

        if (args.length > 0) {
            if (args.length != 1) {
                printAndQuit(executorService, "Invalid parameters, only one parameter is allowed: <cli|gui>");
                return;
            }

            String type = args[0];

            if (type.equals("cli")) {
                gui = false;
            } else if (type.equals("gui")) {
                gui = true;
            } else {
                printAndQuit(executorService, "Invalid client type, allowed types: cli, gui");
                return;
            }
        }

        // Initialize the client after the application has loaded
        Consumer<AbstractClientViewer> postStartup = ClientConnector::new;

        // The viewer should register its own log reader
        // The viewer will call the consumer that will initialize the connector when ready
        if (gui) {
            new GuiClientViewer(executorService, postStartup);
        } else {
            new CliClientViewer(executorService, postStartup);
        }

        logger.start(executorService);
    }

    /**
     * Open and close the logger to print a single message
     * @param executorService The executor service
     * @param message The message
     */
    private void printAndQuit(ExecutorService executorService, String message) {
        Logger logger = Logger.getInstance();
        logger.addReader(new ConsoleLogReader(System.out));
        logger.start(executorService);
        logger.warning(message);
        logger.shutdown();
        executorService.shutdownNow();
    }

}
