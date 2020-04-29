package it.polimi.ingsw.common.events;

import it.polimi.ingsw.common.Coordinates;

/**
 * Abstract class for events which concern the worker interactions
 */
public abstract class AbstractWorkerInteractEvent extends AbstractWorkerEvent {

    private final Coordinates destination;

    public AbstractWorkerInteractEvent(int worker, Coordinates destination) {
        super(worker);

        this.destination = destination;
    }

    public Coordinates getDestination() {
        return destination;
    }

}
