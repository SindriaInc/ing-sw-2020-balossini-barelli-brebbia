package it.polimi.ingsw.common;

import it.polimi.ingsw.model.Game;

public enum ViewEventResponse {

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

    public static ViewEventResponse of(Game.ModelResponse response) {
        return ViewEventResponse.valueOf(response.name());
    }

}
