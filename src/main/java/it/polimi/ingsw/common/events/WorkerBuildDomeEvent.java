package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

public class WorkerBuildDomeEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildDomeEvent(Worker worker, Cell cell) {
        super(worker, cell);
    }
    
}
