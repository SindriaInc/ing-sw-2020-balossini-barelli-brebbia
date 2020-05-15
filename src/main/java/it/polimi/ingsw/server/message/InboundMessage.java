package it.polimi.ingsw.server.message;

import java.util.function.Consumer;

public class InboundMessage {

    private final String message;

    private final String sourcePlayer;

    private final Consumer<String> onLogin;

    public InboundMessage(String message, String sourcePlayer) {
        this.message = message;
        this.sourcePlayer = sourcePlayer;

        this.onLogin = player -> {throw new IllegalStateException();};
    }

    public InboundMessage(String message, String tempName, Consumer<String> onLogin) {
        this.message = message;
        this.sourcePlayer = tempName;

        this.onLogin = onLogin;
    }

    public String getMessage() {
        return message;
    }

    public String getSourcePlayer() {
        return sourcePlayer;
    }

    /**
     * Handle the player login, after it's been verified
     * @param player The player name
     */
    public void onLogin(String player) {
        onLogin.accept(player);
    }

}
