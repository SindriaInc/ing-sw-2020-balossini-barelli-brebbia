package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Worker;

/**
 * Event for a worker spawn
 */
public class WorkerSpawnEvent extends AbstractWorkerEvent {

    public WorkerSpawnEvent(Worker worker) {
        super(worker);
    }

}
