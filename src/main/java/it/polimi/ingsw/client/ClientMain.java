package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CliMain;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.logging.reader.ConsoleLogReader;

public class ClientMain {

    public static final long SLEEP_PERIOD_MS = 100;

    private final CliMain cliMain;

    public ClientMain(String... args) {
        Logger logger = Logger.getInstance();
        logger.addReader(new ConsoleLogReader(System.out));
        logger.start();
        logger.info("Initializing client...");

        boolean gui = false;

        cliMain = new CliMain();
        cliMain.init();
    }

}
