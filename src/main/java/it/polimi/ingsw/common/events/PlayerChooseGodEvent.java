package it.polimi.ingsw.common.events;

/**
 * Event for the god choice by the player
 *
 * View -> Model
 * Model -> View
 */
public class PlayerChooseGodEvent extends AbstractPlayerEvent {

    public PlayerChooseGodEvent(String player) {
        super(player);
    }

}
