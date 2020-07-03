package it.polimi.ingsw.common.logging.reader;

import it.polimi.ingsw.common.logging.ILogReader;

import java.io.PrintStream;

/**
 * A console-based <code>ILogReader</code>
 * Logs are written to the PrintStream specified in the constructor
 */
public class ConsoleLogReader implements ILogReader {

    /**
     * The stream where the logs will be written to
     */
    private final PrintStream out;

    /**
     * Class constructor
     *
     * @param out The output stream
     */
    public ConsoleLogReader(PrintStream out) {
        this.out = out;
    }

    /**
     * @see ILogReader#read(String)
     *
     * The message is printed to the <code>PrintStream</code>
     */
    @Override
    public void read(String message) {
        out.println(message);
    }

}
