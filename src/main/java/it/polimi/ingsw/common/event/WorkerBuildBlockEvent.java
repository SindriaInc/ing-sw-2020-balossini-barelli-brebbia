package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;

import java.util.Map;

/**
 * Event for a worker block built
 *
 * View -> Model
 * Model -> View
 */
public class WorkerBuildBlockEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildBlockEvent(String player, int worker, Coordinates destination) {
        super(player, worker, destination);
    }

    public static WorkerBuildBlockEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        int worker = Integer.parseInt(attributes.get(ATTRIBUTE_ID));
        Coordinates destination = Coordinates.parse(attributes.get(ATTRIBUTE_DESTINATION));
        return new WorkerBuildBlockEvent(player, worker, destination);
    }

}
