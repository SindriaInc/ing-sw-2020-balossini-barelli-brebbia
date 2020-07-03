package it.polimi.ingsw.common.event;

/**
 * Abstract class for events which concern the worker
 */
public abstract class AbstractWorkerEvent extends AbstractPlayerEvent {

    /**
     * The worker id
     */
    private final int id;

    /**
     * Abstract class constructor
     *
     * @param player The owner
     * @param id The worker id
     */
    public AbstractWorkerEvent(String player, int id) {
        super(player);

        this.id = id;
    }

    public int getId() {
        return id;
    }

}
