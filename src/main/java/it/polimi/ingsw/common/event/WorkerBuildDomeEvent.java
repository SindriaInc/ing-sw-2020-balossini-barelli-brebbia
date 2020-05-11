package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;

/**
 * Event for a worker dome built
 *
 * View -> Model
 * Model -> View
 */
public class WorkerBuildDomeEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildDomeEvent(String player, int worker, Coordinates destination) {
        super(player, worker, destination);
    }
    
}
