package it.polimi.ingsw.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

public class Logger {

    /**
     * The logger singleton
     */
    private static final Logger instance = new Logger();

    /**
     * The messages to be processed by the actual logging thread
     */
    private final BlockingDeque<String> pendingMessages = new LinkedBlockingDeque<>();

    /**
     * The list of log readers, which process messages
     */
    private final List<ILogReader> readers = Collections.synchronizedList(new ArrayList<>());

    /**
     * The list of log filters
     * Messages to be logged will be checked against each filter
     * If the message contains the filter, it will not be read
     */
    private final List<String> filters = Collections.synchronizedList(new ArrayList<>());

    /**
     * The logger level, messages with a level lower than the logger level will be ignored
     */
    private LogLevel level = LogLevel.DEBUG; // TODO: Switch to an higher log level in production

    /**
     * Whether or not logging has started processing
     */
    private boolean started = false;

    public static Logger getInstance() {
        return instance;
    }

    private Logger() {}

    /**
     * Starts the logging
     * @throws IllegalStateException if already started
     */
    public void start(ExecutorService executorService) {
        if (started) {
            throw new IllegalStateException();
        }

        executorService.submit(this::process);
        started = true;
    }

    /**
     * Stops the logging
     * @throws IllegalStateException if not started
     */
    public void shutdown() {
        if (!started) {
            throw new IllegalStateException();
        }

        started = false;
    }

    public void addReader(ILogReader reader) {
        readers.add(reader);
    }

    public void removeReader(ILogReader reader) {
        readers.remove(reader);
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warning(String message) {
        log(LogLevel.WARNING, message);
    }

    public void severe(String message) {
        log(LogLevel.SEVERE, message);
    }

    public void exception(Exception exception) {
        String message = "Unexpected exception: " + exception.getMessage();

        // Print the stacktrace if debugging
        if (LogLevel.DEBUG.filter(level)) {
            StringWriter result = new StringWriter();
            PrintWriter writer = new PrintWriter(result);
            exception.printStackTrace(writer);

            String stackTrace = result.toString();
            message += System.lineSeparator() + stackTrace;
        }

        log(LogLevel.SEVERE, message);
    }

    public void log(LogLevel level, String message) {
        if (!level.filter(this.level)) {
            return;
        }

        pendingMessages.addLast("[" + level.name() + "] " + message);
    }

    private void process() {
        while (started) {
            try {
                String message = pendingMessages.take();
                read(message);
            } catch (InterruptedException ignored) {
                // Shutting down
                started = false;
            }
        }

        debug("Logger shutdown");

        while (pendingMessages.size() > 0) {
            String message = pendingMessages.poll();
            read(message);
        }
    }

    private void read(String message) {
        for (String filter : filters) {
            if (message.contains(filter)) {
                return;
            }
        }

        for (ILogReader reader : readers) {
            reader.read(message);
        }
    }

    public void filter(String string) {
        filters.add(string);
    }

}
