package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.model.ModelEventProvider;

/**
 * Request a PlayerEndTurnEvent from the player
 * The player may send another view event if the related request was sent between this and a previous non-request event
 *
 * Notify the player about being able to end the turn
 * This event is sent after every other request during the "on going" game state to signal that no other requests are coming
 */
public class RequestPlayerEndTurnEvent extends AbstractRequestEvent {

    private final boolean canBeEnded;

    public RequestPlayerEndTurnEvent(String player, boolean canBeEnded) {
        super(player);

        this.canBeEnded = canBeEnded;
    }

    public boolean getCanBeEnded() {
        return canBeEnded;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestPlayerEndTurnEventObservable().notifyObservers(this);
    }

}
