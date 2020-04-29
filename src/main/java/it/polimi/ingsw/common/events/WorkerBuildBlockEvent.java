package it.polimi.ingsw.common.events;

import it.polimi.ingsw.common.Coordinates;

/**
 * Event for a worker block built
 *
 * View -> Model
 * Model -> View
 */
public class WorkerBuildBlockEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildBlockEvent(int worker, Coordinates destination) {
        super(worker, destination);
    }

}
