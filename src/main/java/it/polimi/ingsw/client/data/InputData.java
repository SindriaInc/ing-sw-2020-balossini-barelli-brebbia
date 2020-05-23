package it.polimi.ingsw.client.data;

import java.util.Optional;

public class InputData extends AbstractData {

    private final String ip;
    private final Integer port;

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

    public InputData withIp(String ip) {
        return new InputData(null, ip, port);
    }

    public InputData withPort(int port) {
        return new InputData(null, ip, port);
    }

}
