package it.polimi.ingsw.server.message;

import java.util.Optional;

public class ErrorMessage {

    public enum ErrorType {

        INIT_FAILED,
        ACCEPT_FAILED,
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
