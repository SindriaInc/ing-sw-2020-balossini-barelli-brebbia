package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;

import java.util.Map;

/**
 * Event for a worker force move
 *
 * View -> Model
 * Model -> View
 */
public class WorkerForceEvent extends AbstractWorkerInteractEvent {

    public static final String ATTRIBUTE_TARGET = "target";

    private final int target;

    public WorkerForceEvent(String player, int worker, int target, Coordinates destination) {
        super(player, worker, destination);

        this.target = target;
    }

    public int getTarget() {
        return target;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();
        serialized.put(ATTRIBUTE_TARGET, Integer.toString(target));
        return serialized;
    }

    public static WorkerForceEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        int worker = Integer.parseInt(attributes.get(ATTRIBUTE_ID));
        int target = Integer.parseInt(attributes.get(ATTRIBUTE_TARGET));
        Coordinates destination = Coordinates.parse(attributes.get(ATTRIBUTE_DESTINATION));
        return new WorkerForceEvent(player, worker, target, destination);
    }

}
