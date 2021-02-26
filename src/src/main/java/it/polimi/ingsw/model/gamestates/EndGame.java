package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.event.PlayerWinEvent;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * The class representing the last state of game which ends it.
 */
public class EndGame extends AbstractGameState {

    /**
     * Class constructor
     * @param provider The provider of the events
     * @param board The game's board
     * @param winner The winner of the game
     * @param players The game's players
     */
    public EndGame(ModelEventProvider provider, Board board, String winner, List<Player> players) {
        super(provider, board, players);

        var event = new PlayerWinEvent(winner);
        setReceivers(event);
        event.accept(getModelEventProvider());
    }

    /**
     * @see AbstractGameState#getCurrentPlayer()
     */
    @Override
    public Player getCurrentPlayer() {
        return null;
    }

    /**
     * @see AbstractGameState#nextState()
     */
    @Override
    public AbstractGameState nextState() {
        return this;
    }

}
