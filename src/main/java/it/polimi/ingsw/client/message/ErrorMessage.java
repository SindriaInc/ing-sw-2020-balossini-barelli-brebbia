package it.polimi.ingsw.client.message;

import java.util.Optional;

public class ErrorMessage {

    public enum ErrorType {

        INIT_FAILED,
        MESSAGE_FAILED;

    }

    private final ErrorType errorType;

    private final String message;

    public ErrorMessage(ErrorType errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

}
