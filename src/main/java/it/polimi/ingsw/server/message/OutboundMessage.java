package it.polimi.ingsw.server.message;

public class OutboundMessage {

    private final String destinationPlayer;

    private final String message;

    public OutboundMessage(String destinationPlayer, String message) {
        this.destinationPlayer = destinationPlayer;
        this.message = message;
    }

    public String getDestinationPlayer() {
        return destinationPlayer;
    }

    public String getMessage() {
        return message;
    }

}
