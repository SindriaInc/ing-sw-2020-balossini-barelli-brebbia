package it.polimi.ingsw.server.message;

import java.util.Optional;
import java.util.function.Consumer;

public class InboundMessage {

    private final String sourcePlayer;

    private final String message;

    private final Consumer<String> onLogin;

    public InboundMessage(String sourcePlayer, String message) {
        this.sourcePlayer = sourcePlayer;
        this.message = message;

        this.onLogin = (player -> {throw new IllegalStateException();});
    }

    public InboundMessage(String message, Consumer<String> onLogin) {
        this.sourcePlayer = null;
        this.message = message;

        this.onLogin = onLogin;
    }

    public Optional<String> getSourcePlayer() {
        return Optional.ofNullable(sourcePlayer);
    }

    public String getMessage() {
        return message;
    }

    public void onLogin(String player) {
        onLogin.accept(player);
    }

}
