package it.polimi.ingsw.common.event;

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
