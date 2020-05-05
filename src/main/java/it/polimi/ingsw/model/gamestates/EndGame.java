package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.event.PlayerWinEvent;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class EndGame extends AbstractGameState {

    public EndGame(ModelEventProvider provider, Board board, Player winner) {
        super(provider, board, List.of(winner));

        getModelEventProvider().getPlayerWinEventObservable().notifyObservers(
                new PlayerWinEvent(winner.getName())
        );
    }

    @Override
    public Player getCurrentPlayer() {
        return getPlayers().get(0);
    }

    @Override
    public AbstractGameState nextState() {
        return this;
    }

}
