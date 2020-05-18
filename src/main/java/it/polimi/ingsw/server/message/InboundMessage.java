package it.polimi.ingsw.server.message;

public class InboundMessage {

    /**
     * The player sending the message, or the player's temporary fake name if not identified
     */
    private final String sourcePlayer;

    /**
     * The message coming from the player
     */
    private final String message;

    public InboundMessage(String sourcePlayer, String message) {
        this.sourcePlayer = sourcePlayer;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getSourcePlayer() {
        return sourcePlayer;
    }

}
