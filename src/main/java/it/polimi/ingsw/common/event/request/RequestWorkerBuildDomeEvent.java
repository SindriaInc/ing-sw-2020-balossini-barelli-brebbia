package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a RequestWorkerBuildDomeEvent from the player
 * The player may send another view event if the related request was sent between this and a previous non-request event
 */
public class RequestWorkerBuildDomeEvent extends AbstractRequestWorkerInteractEvent {

    public RequestWorkerBuildDomeEvent(String player, int worker, List<Coordinates> availableDestinations) {
        super(player, worker, availableDestinations);
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestWorkerBuildDomeEventObservable().notifyObservers(this);
    }
    
}
