package it.polimi.ingsw.server.message;

public class OutboundMessage {

    /**
     * The player will receive this message, or the player's fake temporary name
     */
    private final String destinationPlayer;

    /**
     * The message to be sent to the player
     */
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
