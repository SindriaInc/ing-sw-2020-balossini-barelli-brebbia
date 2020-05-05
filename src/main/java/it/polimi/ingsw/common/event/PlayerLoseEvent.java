package it.polimi.ingsw.common.event;

import java.util.Map;

/**
 * Event for a player defeat
 *
 * Model -> View
 */
public class PlayerLoseEvent extends AbstractPlayerEvent {

    public PlayerLoseEvent(String player) {
        super(player);
    }

    public static PlayerLoseEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        return new PlayerLoseEvent(player);
    }

}
