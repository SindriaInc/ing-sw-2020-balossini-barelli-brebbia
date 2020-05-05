package it.polimi.ingsw.common.event.request;

import java.util.Map;

/**
 * Notify the player about being able to end the turn
 * This event is sent after every other request during the "on going" game state to signal that no other requests are coming
 */
public class RequestPlayerEndTurnEvent extends AbstractRequestEvent {

    public static final String ATTRIBUTE_CAN_BE_ENDED = "canBeEnded";

    private final boolean canBeEnded;

    public RequestPlayerEndTurnEvent(String player, boolean canBeEnded) {
        super(player);

        this.canBeEnded = canBeEnded;
    }

    public boolean getCanBeEnded() {
        return canBeEnded;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();
        serialized.put(ATTRIBUTE_CAN_BE_ENDED, Boolean.toString(canBeEnded));
        return serialized;
    }

    public static RequestPlayerEndTurnEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        boolean canBeEnded = Boolean.parseBoolean(attributes.get(ATTRIBUTE_CAN_BE_ENDED));
        return new RequestPlayerEndTurnEvent(player, canBeEnded);
    }

}
