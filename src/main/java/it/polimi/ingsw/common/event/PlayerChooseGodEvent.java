package it.polimi.ingsw.common.event;

/**
 * Event for the god choice by the player
 *
 * View -> Model
 * Model -> View
 */
public class PlayerChooseGodEvent extends AbstractPlayerEvent {

    private final String god;

    public PlayerChooseGodEvent(String player, String god) {
        super(player);

        this.god = god;
    }

    public String getGod() {
        return god;
    }

}