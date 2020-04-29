package it.polimi.ingsw.common.events;

import it.polimi.ingsw.common.Coordinates;

/**
 * Event for a worker dome built
 *
 * View -> Model
 * Model -> View
 */
public class WorkerBuildDomeEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildDomeEvent(int worker, Coordinates destination) {
        super(worker, destination);
    }
    
}
