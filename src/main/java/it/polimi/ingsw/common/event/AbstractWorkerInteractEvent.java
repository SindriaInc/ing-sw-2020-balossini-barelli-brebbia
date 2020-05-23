package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.info.Coordinates;

/**
 * Abstract class for events which concern the worker interactions
 */
public abstract class AbstractWorkerInteractEvent extends AbstractWorkerEvent {

    private final Coordinates destination;

    public AbstractWorkerInteractEvent(String player, int worker, Coordinates destination) {
        super(player, worker);

        this.destination = destination;
    }

    public Coordinates getDestination() {
        return destination;
    }

}
