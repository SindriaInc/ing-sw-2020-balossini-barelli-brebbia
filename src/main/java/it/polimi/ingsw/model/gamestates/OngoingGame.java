package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class OngoingGame extends AbstractGameState {

    public OngoingGame(Board board, List<Player> players) {
        super(board, players);
    }

    @Override
    public AbstractGameState nextState() {
        return null;
    }

    @Override
    public Player getCurrentPlayer() {
        return null;
    }

}
