package it.polimi.ingsw.client.data;

import java.util.Optional;

/**
 * Represent the data of the input state
 */
public class InputData extends AbstractData {

    /**
     * The server IP
     */
    private final String ip;

    /**
     * The server port
     */
    private final Integer port;

    /**
     * Class constructor, set last message, IP and port
     *
     * @param lastMessage The last message
     * @param ip The IP address
     * @param port The port number
     */
    public InputData(String lastMessage, String ip, Integer port) {
        super(lastMessage);

        this.ip = ip;
        this.port = port;
    }

    public Optional<String> getIp() {
        return Optional.ofNullable(ip);
    }

    public Optional<Integer> getPort() {
        return Optional.ofNullable(port);
    }

    /**
     * Create a new InputData with a given IP
     * @param ip The IP address
     * @return The new InputData
     */
    public InputData withIp(String ip) {
        return new InputData(null, ip, port);
    }

    /**
     * Create a new InputData with a given port
     * @param port The port
     * @return The new InputData
     */
    public InputData withPort(int port) {
        return new InputData(null, ip, port);
    }

}
