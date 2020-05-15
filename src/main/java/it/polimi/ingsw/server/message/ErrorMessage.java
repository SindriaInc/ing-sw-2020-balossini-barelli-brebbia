package it.polimi.ingsw.server.message;

import java.util.Optional;

public class ErrorMessage {

    public enum ErrorType {

        /**
         * When there was an error while accepting a new player
         */
        ACCEPT_FAILED,

        /**
         * When a message couldn't be sent to a player
         */
        OUTBOUND_MESSAGE_FAILED;

    }

    private final ErrorType errorType;

    private final OutboundMessage outboundMessage;

    public ErrorMessage(ErrorType errorType, OutboundMessage outboundMessage) {
        this.errorType = errorType;
        this.outboundMessage = outboundMessage;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Optional<OutboundMessage> getOutboundMessage() {
        return Optional.ofNullable(outboundMessage);
    }

}
