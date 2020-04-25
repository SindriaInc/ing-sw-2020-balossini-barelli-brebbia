package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Worker;

/**
 * Abstract class for events which concern the worker
 */
public abstract class AbstractWorkerEvent {

    private final Worker worker;

    public AbstractWorkerEvent(Worker worker) {
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }

}
