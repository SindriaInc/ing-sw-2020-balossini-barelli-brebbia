package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

public class WorkerBuildBlockEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildBlockEvent(Worker worker, Cell cell) {
        super(worker, cell);
    }

}
