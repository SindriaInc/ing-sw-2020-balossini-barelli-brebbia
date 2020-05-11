package it.polimi.ingsw.common.event;

/**
 * Abstract class for events which concern the worker
 */
public abstract class AbstractWorkerEvent extends AbstractPlayerEvent {

    private final int id;

    public AbstractWorkerEvent(String player, int id) {
        super(player);

        this.id = id;
    }

    public int getId() {
        return id;
    }

}
