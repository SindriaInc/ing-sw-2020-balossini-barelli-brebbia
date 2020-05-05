package it.polimi.ingsw.common.event.response;

import it.polimi.ingsw.common.event.PlayerLoseEvent;

import java.util.Map;

public class ResponseInvalidParametersEvent extends AbstractResponseEvent {

    public ResponseInvalidParametersEvent(String player) {
        super(player);
    }

    public static PlayerLoseEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        return new PlayerLoseEvent(player);
    }

}
