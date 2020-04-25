package it.polimi.ingsw.common;

import it.polimi.ingsw.model.Game;

public enum ActionEventResponse {

    /**
     * The action is allowed
     */
    ALLOW,

    /**
     * The player is not correct or doesn't exist
     */
    INVALID_PLAYER,

    /**
     * The parameters are not correct
     */
    INVALID_PARAMS,

    /**
     * The state is not correct
     */
    INVALID_STATE;

    public static ActionEventResponse of(Game.ModelResponse response) {
        return ActionEventResponse.valueOf(response.name());
    }

}
