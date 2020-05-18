package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.event.PlayerWinEvent;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class EndGame extends AbstractGameState {

    public EndGame(ModelEventProvider provider, Board board, String winner, List<Player> players) {
        super(provider, board, players);

        var event = new PlayerWinEvent(winner);
        setReceivers(event);
        event.accept(getModelEventProvider());
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
