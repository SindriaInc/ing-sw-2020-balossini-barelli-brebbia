package it.polimi.ingsw.common.events;

import it.polimi.ingsw.common.Coordinates;

/**
 * Event for a worker force move
 *
 * View -> Model
 * Model -> View
 */
public class WorkerForceEvent extends AbstractWorkerInteractEvent {

    private final int target;

    public WorkerForceEvent(int worker, int target, Coordinates destination) {
        super(worker, destination);

        this.target = target;
    }

    public int getTarget() {
        return target;
    }

}
