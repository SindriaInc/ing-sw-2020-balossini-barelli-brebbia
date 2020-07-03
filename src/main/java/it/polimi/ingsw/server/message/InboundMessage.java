package it.polimi.ingsw.server.message;

/**
 * A message received from a player
 */
public class InboundMessage {

    /**
     * The player sending the message, or the player's temporary fake name if not identified
     */
    private final String sourcePlayer;

    /**
     * The message coming from the player
     */
    private final String message;

    /**
     * Class constructor
     *
     * @param sourcePlayer The message source player
     * @param message The message
     */
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
