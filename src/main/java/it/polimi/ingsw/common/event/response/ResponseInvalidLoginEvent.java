package it.polimi.ingsw.common.event.response;

import it.polimi.ingsw.controller.ResponseEventProvider;

/**
 * Event sent when the player sends a login event with an existing username or an invalid age
 * The message sent back should be a displayable message with accurate information about what went wrong
 *
 * This event is handled by the VirtualView.
 */
public class ResponseInvalidLoginEvent extends AbstractResponseEvent {

    private final String message;

    /**
     * Class constructor
     *
     * @param player The player that will receive the event
     */
    public ResponseInvalidLoginEvent(String player, String message) {
        super(player);

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ResponseEventProvider provider) {
        provider.getResponseInvalidLoginEventObservable().notifyObservers(this);
    }

}
