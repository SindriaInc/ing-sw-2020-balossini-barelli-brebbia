package it.polimi.ingsw.server.message;

import java.util.Optional;
import java.util.function.Consumer;

public class InboundMessage {

    private final String sourcePlayer;

    private final String message;

    private final Consumer<String> onLogin;

    private final Runnable onLoginFail;

    public InboundMessage(String sourcePlayer, String message) {
        this.sourcePlayer = sourcePlayer;
        this.message = message;

        this.onLogin = player -> {throw new IllegalStateException();};
        this.onLoginFail = () -> {throw new IllegalStateException();};
    }

    public InboundMessage(String message, Consumer<String> onLogin, Runnable onLoginFail) {
        this.sourcePlayer = null;
        this.message = message;

        this.onLogin = onLogin;
        this.onLoginFail = onLoginFail;
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

    public void onLoginFail() {
        onLoginFail.run();
    }

}
