package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a RequestWorkerMoveEvent from the player
 * The player may send another view event if the related request was sent between this and a previous non-request event
 */
public class RequestWorkerMoveEvent extends AbstractRequestWorkerInteractEvent {

    /**
     * Class constructor
     *
     * @param player The player that the request is targeted to
     * @param worker The worker that can be moved
     * @param availableDestinations The list of coordinates where the worker can move to
     */
    public RequestWorkerMoveEvent(String player, int worker, List<Coordinates> availableDestinations) {
        super(player, worker, availableDestinations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestWorkerMoveEventObservable().notifyObservers(this);
    }

}
