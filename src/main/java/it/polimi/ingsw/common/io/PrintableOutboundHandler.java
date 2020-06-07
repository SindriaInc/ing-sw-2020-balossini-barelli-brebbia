package it.polimi.ingsw.common.io;

import java.io.PrintStream;
import java.util.function.Consumer;

public class PrintableOutboundHandler extends OutboundHandler {

    private final PrintStream outputStream;

    public PrintableOutboundHandler(PrintStream outputStream, Consumer<String> errorMessageConsumer) {
        super(outputStream, errorMessageConsumer);

        this.outputStream = outputStream;
    }

    /**
     * Prints the message using the PrintStream's print line method
     * @param message The message
     */
    @Override
    protected void print(String message) {
        outputStream.println(message);
    }

}
