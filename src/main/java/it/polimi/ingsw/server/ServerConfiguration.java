package it.polimi.ingsw.server;

import java.nio.file.Path;

public class ServerConfiguration {

    private static final int DEFAULT_PORT = 25565;

    private static final String DEFAULT_LOG_PATH = "../log.txt";

    private final int port;

    private final String logPath;

    public static ServerConfiguration fromFile(Path path) {
        // TODO: Implement loading
        return new ServerConfiguration(DEFAULT_PORT, DEFAULT_LOG_PATH);
    }

    public ServerConfiguration(int port, String logPath) {
        this.port = port;
        this.logPath = logPath;
    }

    public int getPort() {
        return port;
    }

    public String getLogPath() {
        return logPath;
    }

}
