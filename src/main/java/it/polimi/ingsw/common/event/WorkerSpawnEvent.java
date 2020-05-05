package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;

import java.util.Map;

/**
 * Event for a worker spawn
 *
 * View -> Model
 * Model -> View
 */
public class WorkerSpawnEvent extends AbstractWorkerEvent {

    public static final String ATTRIBUTE_POSITION = "position";

    private final Coordinates position;

    public WorkerSpawnEvent(String player, int id, Coordinates position) {
        super(player, id);

        this.position = position;
    }

    public Coordinates getPosition() {
        return position;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();
        serialized.put(ATTRIBUTE_POSITION, position.toString());
        return serialized;
    }

    public static WorkerBuildDomeEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        int worker = Integer.parseInt(attributes.get(ATTRIBUTE_ID));
        Coordinates destination = Coordinates.parse(attributes.get(ATTRIBUTE_POSITION));
        return new WorkerBuildDomeEvent(player, worker, destination);
    }

}
