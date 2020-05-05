package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;

import java.util.Map;

/**
 * Abstract class for events which concern the worker interactions
 */
public abstract class AbstractWorkerInteractEvent extends AbstractWorkerEvent {

    public static final String ATTRIBUTE_DESTINATION = "destination";

    private final Coordinates destination;

    public AbstractWorkerInteractEvent(String player, int worker, Coordinates destination) {
        super(player, worker);

        this.destination = destination;
    }

    public Coordinates getDestination() {
        return destination;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();
        serialized.put(ATTRIBUTE_DESTINATION, destination.toString());
        return serialized;
    }

}
