package it.polimi.ingsw.common;

import it.polimi.ingsw.model.Game;

public enum ActionEventResponse {

    ALLOW,
    INVALID_PLAYER,
    INVALID_PARAMS,
    INVALID_STATE;

    public static ActionEventResponse of(Game.ModelResponse response) {
        return ActionEventResponse.valueOf(response.name());
    }

}
