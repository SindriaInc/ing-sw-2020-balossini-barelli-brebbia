package it.polimi.ingsw.client.message;

import java.util.Optional;

public class ErrorMessage {

    public enum ErrorType {

        /**
         * When a message couldn't be sent to the server
         */
        MESSAGE_FAILED;

    }

    /**
     * The type of the error message, for more info see ErrorMessage.ErrorType
     */
    private final ErrorType errorType;

    /**
     * The message that was being sent
     */
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
