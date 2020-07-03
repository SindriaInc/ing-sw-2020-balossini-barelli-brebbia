package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a RequestWorkerBuildDomeEvent from the player
 * The player may send another view event if the related request was sent between this and a previous non-request event
 */
public class RequestWorkerBuildDomeEvent extends AbstractRequestWorkerInteractEvent {

    /**
     * Class constructor
     *
     * @param player The player that the request is targeted to
     * @param worker The worker that can be moved
     * @param availableDestinations The list of coordinates where the worker can build a dome
     */
    public RequestWorkerBuildDomeEvent(String player, int worker, List<Coordinates> availableDestinations) {
        super(player, worker, availableDestinations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestWorkerBuildDomeEventObservable().notifyObservers(this);
    }
    
}
