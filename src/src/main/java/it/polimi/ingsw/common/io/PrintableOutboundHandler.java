package it.polimi.ingsw.common.io;

import java.io.PrintStream;
import java.util.function.Consumer;

/**
 * An outbound handler that is specifically made to use a <code>PrintStream</code>, to bypass character encoding
 */
public class PrintableOutboundHandler extends OutboundHandler {

    /**
     * The output stream
     */
    private final PrintStream outputStream;

    /**
     * Class constructor
     *
     * @param outputStream The output stream
     * @param errorMessageConsumer The error message handler
     */
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
