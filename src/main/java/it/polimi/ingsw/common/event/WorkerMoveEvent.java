package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;

/**
 * Event for a worker movement
 *
 * View -> Model
 * Model -> View
 */
public class WorkerMoveEvent extends AbstractWorkerInteractEvent {

    public WorkerMoveEvent(String player, int worker, Coordinates coordinates) {
        super(player, worker, coordinates);
    }

}
