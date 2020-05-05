package it.polimi.ingsw.common.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for events which concern the player
 */
public abstract class AbstractPlayerEvent extends AbstractEvent {

    public static final String ATTRIBUTE_PLAYER = "player";

    private final String player;

    public AbstractPlayerEvent(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = new HashMap<String, String>();
        serialized.put(ATTRIBUTE_PLAYER, player);
        return serialized;
    }

}
