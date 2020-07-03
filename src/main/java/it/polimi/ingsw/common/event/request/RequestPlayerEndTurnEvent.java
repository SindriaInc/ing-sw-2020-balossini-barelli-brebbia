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

    /**
     * Whether or not the player can end the turn
     */
    private final boolean canBeEnded;

    /**
     * Class constructor
     *
     * @param player The player that the request is targeted to
     * @param canBeEnded Whether or not the player can actually end the turn
     */
    public RequestPlayerEndTurnEvent(String player, boolean canBeEnded) {
        super(player);

        this.canBeEnded = canBeEnded;
    }

    public boolean getCanBeEnded() {
        return canBeEnded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestPlayerEndTurnEventObservable().notifyObservers(this);
    }

}
