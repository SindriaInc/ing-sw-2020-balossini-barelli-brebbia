package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;

/**
 * Event for a worker spawn
 *
 * View -> Model
 * Model -> View
 */
public class WorkerSpawnEvent extends AbstractWorkerEvent {

    private final Coordinates position;

    public WorkerSpawnEvent(String player, int id, Coordinates position) {
        super(player, id);

        this.position = position;
    }

    public Coordinates getPosition() {
        return position;
    }

}
