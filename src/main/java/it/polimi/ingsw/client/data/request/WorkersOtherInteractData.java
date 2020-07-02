package it.polimi.ingsw.client.data.request;

import java.util.Map;
import java.util.Optional;

/**
 * Represent the data of worker interactions that each of the workers can do
 */
public class WorkersOtherInteractData {

    /**
     * The interactions on other workers that each of the worker can do
     * The keys of the map are the worker ids
     */
    private final Map<Integer, WorkersInteractData> availableOtherInteractions;

    /**
     * Class constructor, set the availabel interactions
     *
     * @param availableOtherInteractions The available interactions
     */
    public WorkersOtherInteractData(Map<Integer, WorkersInteractData> availableOtherInteractions) {
        this.availableOtherInteractions = Map.copyOf(availableOtherInteractions);
    }

    public Map<Integer, WorkersInteractData> getAvailableOtherInteractions() {
        return availableOtherInteractions;
    }

    /**
     * Get single worker interaction
     * @return The interaction, if exists
     */
    public Optional<Map.Entry<Integer, WorkersInteractData>> getSingleWorkerOtherInteractions() {
        if (availableOtherInteractions.size() != 1) {
            return Optional.empty();
        }

        return Optional.of(availableOtherInteractions.entrySet().iterator().next());
    }


}
