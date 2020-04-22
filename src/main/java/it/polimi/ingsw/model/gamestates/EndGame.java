package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class EndGame extends AbstractGameState {

    public EndGame(Board board, Player winner) {
        super(board, List.of(winner));
    }

    @Override
    public Player getCurrentPlayer() {
        return getPlayers().get(0);
    }

    @Override
    public AbstractGameState nextState() {
        return this;
    }

    @Override
    public boolean isEnded() {
        return true;
    }

}
