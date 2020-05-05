package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;

import java.util.Map;

/**
 * Event for a worker movement
 *
 * View -> Model
 * Model -> View
 */
public class WorkerMoveEvent extends AbstractWorkerInteractEvent {

    public WorkerMoveEvent(String player, int worker, Coordinates coordinates) {
        super(player, worker, coordinates);
    }

    public static WorkerBuildDomeEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        int worker = Integer.parseInt(attributes.get(ATTRIBUTE_ID));
        Coordinates destination = Coordinates.parse(attributes.get(ATTRIBUTE_DESTINATION));
        return new WorkerBuildDomeEvent(player, worker, destination);
    }

}
