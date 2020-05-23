package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;
import java.util.Map;

/**
 * Request a RequestWorkerForceEvent from the player
 * The player may send another view event if the related request was sent between this and a previous non-request event
 */
public class RequestWorkerForceEvent extends AbstractRequestEvent {

    private final int worker;

    private final Map<Integer, List<Coordinates>> availableTargetDestinations;

    public RequestWorkerForceEvent(String player, int worker, Map<Integer, List<Coordinates>> availableTargetDestinations) {
        super(player);

        this.worker = worker;
        this.availableTargetDestinations = availableTargetDestinations;
    }

    public int getWorker() {
        return worker;
    }

    public Map<Integer, List<Coordinates>> getAvailableTargetDestinations() {
        return availableTargetDestinations;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestWorkerForceEventObservable().notifyObservers(this);
    }

}
