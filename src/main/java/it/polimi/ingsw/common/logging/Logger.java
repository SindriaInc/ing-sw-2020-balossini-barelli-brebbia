package it.polimi.ingsw.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class Logger {

    private static final String THREAD_NAME = "Logging";

    private static final long SLEEP_PERIOD_MS = 10;

    /**
     * The logger singleton
     */
    private static final Logger instance = new Logger();

    /**
     * The messages to be processed by the actual logging thread
     */
    private final Deque<String> pendingMessages = new LinkedBlockingDeque<>();

    /**
     * The list of log readers, which process messages
     */
    private final List<ILogReader> readers = new ArrayList<>();

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
    public void start() {
        if (started) {
            throw new IllegalStateException();
        }

        new Thread(this::process, THREAD_NAME).start();
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
        String message = exception.getMessage();

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
            while (pendingMessages.size() > 0) {
                String message = pendingMessages.poll();
                process(message);
            }

            try {
                Thread.sleep(SLEEP_PERIOD_MS);
            } catch (InterruptedException ignored) {}
        }

        debug("Logger shutdown");

        while (pendingMessages.size() > 0) {
            String message = pendingMessages.poll();
            process(message);
        }
    }

    private void process(String message) {
        for (ILogReader reader : readers) {
            reader.read(message);
        }
    }

}
