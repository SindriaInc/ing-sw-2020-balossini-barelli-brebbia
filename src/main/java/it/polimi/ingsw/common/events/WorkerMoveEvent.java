package it.polimi.ingsw.common.events;

import it.polimi.ingsw.common.Coordinates;

/**
 * Event for a worker movement
 *
 * View -> Model
 * Model -> View
 */
public class WorkerMoveEvent extends AbstractWorkerInteractEvent {

    public WorkerMoveEvent(int worker, Coordinates coordinates) {
        super(worker, coordinates);
    }

}
