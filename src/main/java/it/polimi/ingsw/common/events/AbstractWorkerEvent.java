package it.polimi.ingsw.common.events;

/**
 * Abstract class for events which concern the worker
 */
public abstract class AbstractWorkerEvent {

    private final int id;

    public AbstractWorkerEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
