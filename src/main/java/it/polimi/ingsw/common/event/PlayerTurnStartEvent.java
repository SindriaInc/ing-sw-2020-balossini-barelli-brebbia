package it.polimi.ingsw.common.event;

import java.util.Map;

/**
 * Event for the turn start
 *
 * Model -> View
 */
public class PlayerTurnStartEvent extends AbstractPlayerEvent {

    public PlayerTurnStartEvent(String player) {
        super(player);
    }

    public static PlayerTurnStartEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        return new PlayerTurnStartEvent(player);
    }

}
