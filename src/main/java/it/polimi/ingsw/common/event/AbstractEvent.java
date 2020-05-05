package it.polimi.ingsw.common.event;

import java.util.Map;

public abstract class AbstractEvent {

    public abstract Map<String, String> serializeAttributes();

}
