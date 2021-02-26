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

    /**
     * The worker that can be used to force
     */
    private final int worker;

    /**
     * The map of workers that can be forced (as the map keys) and their related coordinates to where they can be
     * forced to (as the map values)
     */
    private final Map<Integer, List<Coordinates>> availableTargetDestinations;

    /**
     * Class constructor
     *
     * @param player The player that the request is targeted to
     * @param worker The worker that can be use to force
     * @param availableTargetDestinations The map of other workers and coordinates to where they can be forced to
     */
    public RequestWorkerForceEvent(String player, int worker, Map<Integer, List<Coordinates>> availableTargetDestinations) {
        super(player);

        this.worker = worker;
        this.availableTargetDestinations = availableTargetDestinations;
    }

    public int getWorker() {
        return worker;
    }

    /**
     * A copy of the map of workers that can be forced
     *
     * @return The map
     */
    public Map<Integer, List<Coordinates>> getAvailableTargetDestinations() {
        return Map.copyOf(availableTargetDestinations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestWorkerForceEventObservable().notifyObservers(this);
    }

}
