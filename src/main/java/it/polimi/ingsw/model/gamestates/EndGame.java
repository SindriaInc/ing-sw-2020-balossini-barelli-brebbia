package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.event.PlayerWinEvent;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class EndGame extends AbstractGameState {

    private final String winner;

    public EndGame(ModelEventProvider provider, Board board, String winner, List<Player> players) {
        super(provider, board, players);
        this.winner = winner;

        var event = new PlayerWinEvent(winner);
        setReceivers(event);
        getModelEventProvider().getPlayerWinEventObservable().notifyObservers(event);
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
