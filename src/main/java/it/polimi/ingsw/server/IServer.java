package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.OutboundMessage;

public interface IServer {

    /**
     * Messages from non-identified players have the player set as this string followed by a unique number
     */
    String UNIDENTIFIED_PLAYER_PREFIX = "#UNKNOWN:";

    /**
     * Send a message to a player
     * The message will reach the player in an unspecified amount of time
     * If there's an error while sending the message, the error handlers will be notified
     * @param message The message
     */
    void send(OutboundMessage message);

    /**
     * Register a connect handler
     * @param receiver The IConnectHandler
     */
    void registerHandler(IConnectHandler receiver);

    /**
     * Register a message handler
     * @param receiver The IMessageHandler
     */
    void registerHandler(IMessageHandler receiver);

    /**
     * Register a error handler
     * @param receiver The IErrorHandler
     */
    void registerHandler(IErrorHandler receiver);

    /**
     * Identify a temporary fake name with the actual player name
     * @param tempName The temporary name
     * @param player The player name
     */
    void identify(String tempName, String player);

    /**
     * Disconnect a player
     * @param player The player's name or the fake temporary name
     */
    void disconnect(String player);

    /**
     * Shut down the server, disconnecting every player
     */
    void shutdown();

    /**
     * Checks if the player is identified
     * @param player The player, or the fake temporary name
     * @return true if the player does not have a temporary name
     */
    static boolean isIdentified(String player) {
        return !player.startsWith(UNIDENTIFIED_PLAYER_PREFIX);
    }

}
