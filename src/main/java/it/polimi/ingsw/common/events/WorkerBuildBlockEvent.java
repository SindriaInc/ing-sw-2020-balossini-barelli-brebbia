package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * Event for a worker block built
 */
public class WorkerBuildBlockEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildBlockEvent(Worker worker, Cell cell) {
        super(worker, cell);
    }

}
