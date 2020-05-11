package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;

/**
 * Event for a worker block built
 *
 * View -> Model
 * Model -> View
 */
public class WorkerBuildBlockEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildBlockEvent(String player, int worker, Coordinates destination) {
        super(player, worker, destination);
    }

}
