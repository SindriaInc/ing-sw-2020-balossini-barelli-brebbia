package it.polimi.ingsw.client.data.request;

import java.util.Map;
import java.util.Optional;

/**
 * Represent the worker interaction data
 */
public class WorkersInteractData {

    /**
     * The interactions that each of the worker can do
     * The keys of the map are the worker ids
     */
    private final Map<Integer, InteractData> availableInteractions;

    /**
     * Class constructor, set the available interaction
     *
     * @param availableInteractions The available interactions
     */
    public WorkersInteractData(Map<Integer, InteractData> availableInteractions) {
        this.availableInteractions = Map.copyOf(availableInteractions);
    }

    public Map<Integer, InteractData> getAvailableInteractions() {
        return availableInteractions;
    }

    /**
     * Get single worker interaction
     * @return The interaction, if exists
     */
    public Optional<Map.Entry<Integer, InteractData>> getSingleWorkerInteraction() {
        if (availableInteractions.size() != 1) {
            return Optional.empty();
        }

        return Optional.of(availableInteractions.entrySet().iterator().next());
    }

}
