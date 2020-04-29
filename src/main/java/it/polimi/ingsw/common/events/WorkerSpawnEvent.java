package it.polimi.ingsw.common.events;

/**
 * Event for a worker spawn
 *
 * View -> Model
 * Model -> View
 */
public class WorkerSpawnEvent extends AbstractWorkerEvent {

    private final String owner;

    public WorkerSpawnEvent(int id, String owner) {
        super(id);

        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

}
