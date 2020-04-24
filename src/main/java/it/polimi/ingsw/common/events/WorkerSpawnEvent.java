package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Worker;

public class WorkerSpawnEvent extends AbstractWorkerEvent {

    public WorkerSpawnEvent(Worker worker) {
        super(worker);
    }

}
