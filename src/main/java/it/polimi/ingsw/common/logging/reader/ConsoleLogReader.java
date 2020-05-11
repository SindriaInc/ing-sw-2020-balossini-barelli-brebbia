package it.polimi.ingsw.common.logging.reader;

import it.polimi.ingsw.common.logging.ILogReader;

import java.io.PrintStream;

public class ConsoleLogReader implements ILogReader {

    private final PrintStream out;

    public ConsoleLogReader(PrintStream out) {
        this.out = out;
    }

    @Override
    public void read(String message) {
        out.println(message);
    }

}
