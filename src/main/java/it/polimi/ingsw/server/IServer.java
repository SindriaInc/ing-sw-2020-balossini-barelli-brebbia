package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.OutboundMessage;

public interface IServer {

    String UNIDENTIFIED_PLAYER_PREFIX = "#UNKNOWN:";

    void send(OutboundMessage message);

    void registerHandler(IConnectHandler receiver);

    void registerHandler(IMessageHandler receiver);

    void registerHandler(IErrorHandler receiver);

    void disconnect(String player);

    void shutdown();

    static boolean isIdentified(String player) {
        return !player.startsWith(UNIDENTIFIED_PLAYER_PREFIX);
    }

}
