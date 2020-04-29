package it.polimi.ingsw.common.events.requests;

/**
 * Notify the player about being able to end the turn
 * This event is sent after every other request during the "on going" game state to signal that no other requests are coming
 */
public class RequestPlayerEndTurnEvent {

    private final boolean canBeEnded;

    public RequestPlayerEndTurnEvent(boolean canBeEnded) {
        this.canBeEnded = canBeEnded;
    }

    public boolean getCanBeEnded() {
        return canBeEnded;
    }

}
