package it.polimi.ingsw.model;

public enum ModelResponse {

    /**
     * The model has accepted and applied the change
     */
    ALLOW,

    /**
     * One or more of the parameters are not valid
     */
    INVALID_PARAMS,

    /**
     * The method called is not valid in the current state
     */
    INVALID_STATE

}
