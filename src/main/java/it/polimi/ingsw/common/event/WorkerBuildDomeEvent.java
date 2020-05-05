package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;

import java.util.Map;

/**
 * Event for a worker dome built
 *
 * View -> Model
 * Model -> View
 */
public class WorkerBuildDomeEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildDomeEvent(String player, int worker, Coordinates destination) {
        super(player, worker, destination);
    }

    public static WorkerBuildDomeEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        int worker = Integer.parseInt(attributes.get(ATTRIBUTE_ID));
        Coordinates destination = Coordinates.parse(attributes.get(ATTRIBUTE_DESTINATION));
        return new WorkerBuildDomeEvent(player, worker, destination);
    }
    
}
