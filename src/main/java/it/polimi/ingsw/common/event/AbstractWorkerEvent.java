package it.polimi.ingsw.common.event;

import java.util.Map;

/**
 * Abstract class for events which concern the worker
 */
public abstract class AbstractWorkerEvent extends AbstractPlayerEvent {

    public static final String ATTRIBUTE_ID = "id";

    private final int id;

    public AbstractWorkerEvent(String player, int id) {
        super(player);

        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();
        serialized.put(ATTRIBUTE_ID, Integer.toString(id));
        return serialized;
    }

}
