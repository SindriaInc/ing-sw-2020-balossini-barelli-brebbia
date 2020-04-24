package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

public class WorkerMoveEvent extends AbstractWorkerInteractEvent {

    public WorkerMoveEvent(Worker worker, Cell cell) {
        super(worker, cell);
    }

}
