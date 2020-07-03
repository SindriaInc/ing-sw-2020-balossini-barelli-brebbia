package it.polimi.ingsw.common.info;

/**
 * A simplified version of a <code>Worker</code>, only containing serializable fields
 * To be used to share information via events
 */
public class WorkerInfo {

    /**
     * The worker id
     */
    private final int id;

    /**
     * The worker's owner
     */
    private final String owner;

    /**
     * The worker position
     */
    private final Coordinates position;

    /**
     * Class constructor
     *
     * @param id The worker id
     * @param owner The worker's owner
     * @param position The worker position
     */
    public WorkerInfo(int id, String owner, Coordinates position) {
        this.id = id;
        this.owner = owner;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public Coordinates getPosition() {
        return position;
    }

}
