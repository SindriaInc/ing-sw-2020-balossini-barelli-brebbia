package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

public class WorkerForceEvent extends AbstractWorkerInteractEvent {

    private final Worker target;

    public WorkerForceEvent(Worker worker, Worker target, Cell cell) {
        super(worker, cell);

        this.target = target;
    }

    public Worker getTarget() {
        return target;
    }

}
