package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CliMain;
import it.polimi.ingsw.client.cli.InputHandler;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.logging.reader.ConsoleLogReader;

import java.util.concurrent.Executors;

public class ClientMain {

    public static final long SLEEP_PERIOD_MS = 100;

    /**
     * The factory pattern
     */
    private FactoryPattern factory;

    /**
     * The Client
     */
    private Client client;

    /**
     * The asynchronous input handler
     */
    private InputHandler inputHandler;

    private final CliMain cliMain;

    public ClientMain(String... args) {
        Logger logger = Logger.getInstance();
        logger.addReader(new ConsoleLogReader(System.out));
        logger.filter("\"RequestPlayerPingEvent\"");
        logger.filter("\"PlayerPingEvent\"");
        logger.start(Executors.newCachedThreadPool());
        logger.info("Initializing client...");

        boolean gui = false;

        cliMain = new CliMain();
        cliMain.init();

//        this.factory = new FactoryPattern(gui);
//        this.client = new Client(factory.UsableFunctions(inputHandler));
    }

}
