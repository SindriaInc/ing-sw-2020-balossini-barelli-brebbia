package it.polimi.ingsw.common.event;

import java.util.Map;

/**
 * Event for the turn end
 *
 * View -> Model
 */
public class PlayerEndTurnEvent extends AbstractPlayerEvent  {

    public PlayerEndTurnEvent(String player) {
        super(player);
    }

    public static PlayerEndTurnEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        return new PlayerEndTurnEvent(player);
    }

}
