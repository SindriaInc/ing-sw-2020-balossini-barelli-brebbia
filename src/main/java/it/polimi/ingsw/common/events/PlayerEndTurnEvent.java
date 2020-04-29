package it.polimi.ingsw.common.events;

/**
 * Event for the turn end
 *
 * View -> Model
 */
public class PlayerEndTurnEvent extends AbstractPlayerEvent  {

    public PlayerEndTurnEvent(String player) {
        super(player);
    }

}
