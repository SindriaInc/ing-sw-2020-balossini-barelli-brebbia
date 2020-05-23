package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CliClientViewer;
import it.polimi.ingsw.client.gui.GuiClientViewer;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.logging.reader.ConsoleLogReader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientMain {

    public static final long SLEEP_PERIOD_MS = 100;

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

        // The viewer should register its own log reader
        // The viewer will initialize the client connector when ready
        if (gui) {
            new GuiClientViewer(executorService);
        } else {
            new CliClientViewer(executorService);
        }

        logger.start(executorService);
    }

    private void printAndQuit(ExecutorService executorService, String message) {
        Logger logger = Logger.getInstance();
        logger.addReader(new ConsoleLogReader(System.out));
        logger.start(executorService);
        logger.warning(message);
        logger.shutdown();
        executorService.shutdownNow();
    }

}
