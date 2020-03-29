package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.decorators.BlockOnPlayerMoveUp;

public class DefaultAbilities implements IAbilities {

    public static final int DEFAULT_WIN_LEVEL = 3;
    public static final int DEFAULT_DOME_LEVEL = 3;
    private final Board board;
    private final Player player;

    public DefaultAbilities(Board board, Player player) {
        this.board = board;
        this.player = player;
    }

    // TODO: Implement method
    @Override
    public boolean checkHasWon() { return false; }

    // TODO: Implement method
    @Override
    public boolean checkCanMove(Worker worker, Cell cell) { return false; }

    // TODO: Implement method
    @Override
    public void doMove(Worker worker, Cell cell) { }

    // TODO: Implement method
    @Override
    public boolean checkCanBuildBlock(Worker worker, Cell cell) { return false; }

    // TODO: Implement method
    @Override
    public void doBuildBlock(Worker worker, Cell cell) { }

    // TODO: Implement method
    @Override
    public boolean checkCanBuildDome(Worker worker, Cell cell) { return false; }

    // TODO: Implement method
    @Override
    public void doBuildDome(Worker worker, Cell cell) { }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

}
