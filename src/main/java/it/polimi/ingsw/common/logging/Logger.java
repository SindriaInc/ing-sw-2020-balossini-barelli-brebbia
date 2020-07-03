package it.polimi.ingsw.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The Logger provides a service able to take messages to print in a thread-safe way and passing them to one or more
 * <code>ILogReader</code> instances, keeping the messages order and only calling the reader from a single thread
 */
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
    private LogLevel level = LogLevel.INFO;

    /**
     * Whether or not logging has started processing
     */
    private boolean started = false;

    /**
     * Obtains the singleton instance
     *
     * @return The logger instance
     */
    public static Logger getInstance() {
        return instance;
    }

    /**
     * Sole constructor, instances the singleton
     */
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

    /**
     * Adds a reader that will take log messages
     *
     * @param reader The reader
     */
    public void addReader(ILogReader reader) {
        readers.add(reader);
    }

    /**
     * Removes an existing reader
     * The reader will no longer receive log messages
     *
     * @param reader The reader
     */
    public void removeReader(ILogReader reader) {
        readers.remove(reader);
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    /**
     * Logs a message with the severity set as <code>LogLevel.DEBUG</code>
     *
     * @param message The message
     */
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * Logs a message with the severity set as <code>LogLevel.INFO</code>
     *
     * @param message The message
     */
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * Logs a message with the severity set as <code>LogLevel.WARNING</code>
     *
     * @param message The message
     */
    public void warning(String message) {
        log(LogLevel.WARNING, message);
    }

    /**
     * Logs a message with the severity set as <code>LogLevel.SEVERE</code>
     *
     * @param message The message
     */
    public void severe(String message) {
        log(LogLevel.SEVERE, message);
    }


    /**
     * Logs an exception, printing the exception message
     * If the current logger level is <code>LogLevel.DEBUG</code> the stacktrace will also be printed
     *
     * @param exception The exception
     */
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

    /**
     * Logs a message with the severity set the one specified
     *
     * @param level The severity of the message
     * @param message The message
     */
    public void log(LogLevel level, String message) {
        if (!level.filter(this.level)) {
            return;
        }

        pendingMessages.addLast("[" + level.name() + "] " + message);
    }

    /**
     * Reads messages to be printed and sends them to the log readers
     */
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

    /**
     * Sends filtered messages to the log readers
     *
     * @param message The message
     */
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

    /**
     * Adds a filter to the logger
     * Messages that contain the filter string will not be printed
     *
     * @param string The filter string
     */
    public void filter(String string) {
        filters.add(string);
    }

}
