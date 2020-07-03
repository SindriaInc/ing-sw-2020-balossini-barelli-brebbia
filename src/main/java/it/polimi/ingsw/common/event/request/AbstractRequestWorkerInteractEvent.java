package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.info.Coordinates;

import java.util.List;

/**
 * Events that relate to a worker interacting with a given coordinate
 */
public abstract class AbstractRequestWorkerInteractEvent extends AbstractRequestEvent {

    /**
     * The worker that can interact
     */
    private final int worker;

    /**
     * The destinations available for the interaction
     */
    private final List<Coordinates> availableDestinations;

    /**
     * Abstract class constructor
     *
     * @param player The player that the request is targeted to
     * @param worker The worker that can be moved
     * @param availableDestinations The list of coordinates that the worker can interact with
     */
    public AbstractRequestWorkerInteractEvent(String player, int worker, List<Coordinates> availableDestinations) {
        super(player);

        this.worker = worker;
        this.availableDestinations = availableDestinations;
    }

    public int getWorker() {
        return worker;
    }

    /**
     * A copy of the list of available destinations
     *
     * @return The list
     */
    public List<Coordinates> getAvailableDestinations() {
        return List.copyOf(availableDestinations);
    }

}
