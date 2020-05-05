package it.polimi.ingsw.common.event;

import java.util.Map;

/**
 * Event for a player victory
 *
 * Model -> View
 */
public class PlayerWinEvent extends AbstractPlayerEvent {

    public PlayerWinEvent(String player) {
        super(player);
    }

    public static PlayerWinEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        return new PlayerWinEvent(player);
    }

}
