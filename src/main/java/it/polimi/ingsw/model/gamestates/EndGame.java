package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class EndGame extends AbstractGameState {

    public EndGame(Board board, List<Player> players) {
        super(board, players);
    }

    @Override
    public Player getCurrentPlayer() {
        return null;
    }

    @Override
    public AbstractGameState nextState() {
        return this;
    }

}
