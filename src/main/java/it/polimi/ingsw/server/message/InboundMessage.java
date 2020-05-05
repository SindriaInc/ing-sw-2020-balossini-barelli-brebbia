package it.polimi.ingsw.server.message;

public class InboundMessage {

    private final String sourcePlayer;

    private final String message;

    public InboundMessage(String sourcePlayer, String message) {
        this.sourcePlayer = sourcePlayer;
        this.message = message;
    }

    public String getSourcePlayer() {
        return sourcePlayer;
    }

    public String getMessage() {
        return message;
    }

}
