package it.polimi.ingsw.server;

import java.nio.file.Path;

public class ServerConfiguration {

    private static final int DEFAULT_PORT = 25565;

    private final int port;

    public static ServerConfiguration fromFile(Path path) {
        // TODO: Implement loading
        return new ServerConfiguration(DEFAULT_PORT);
    }

    private ServerConfiguration(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

}
