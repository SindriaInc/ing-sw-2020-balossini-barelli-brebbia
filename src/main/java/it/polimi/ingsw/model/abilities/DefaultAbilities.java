package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.*;

import java.util.List;

public class DefaultAbilities implements IAbilities {

    public static final int DEFAULT_WIN_LEVEL = 3;
    public static final int DEFAULT_DOME_LEVEL = 3;
    public static final int DEFAULT_MAX_BUILD_LEVEL = 3;
    public static final int DEFAULT_MAX_UP = 1;
    public static final int DEFAULT_MAX_MOVES = 1;
    public static final int DEFAULT_MAX_BUILDS = 1;

    @Override
    public boolean checkHasWon(List<Worker> workers) {
        for (Worker worker : workers) {
            if (worker.hasMovedUp() && worker.getCell().getLevel() >= DEFAULT_WIN_LEVEL) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        Worker worker = turn.getWorker();

        if (hasMoved(turn)) {
            return false;
        }

        if (cell.getLevel() - worker.getCell().getLevel() > DEFAULT_MAX_UP) {
            return false;
        }

        return canInteract(turn, cell);
    }

    @Override
    public void doMove(Turn turn, Cell cell) {
        turn.addMovement(cell);
        turn.getWorker().move(cell);
    }

    @Override
    public boolean checkCanBuildBlock(Turn turn, Cell cell) {
        if (!canBuild(turn)) {
            return false;
        }

        if (cell.getLevel() >= DEFAULT_MAX_BUILD_LEVEL) {
            return false;
        }

        return canInteract(turn, cell);
    }

    @Override
    public void doBuildBlock(Turn turn, Cell cell) {
        turn.addBlockPlaced(cell);
        cell.setLevel(cell.getLevel() + 1);
    }

    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        if (!canBuild(turn)) {
            return false;
        }

        if (cell.isDoomed() || cell.getLevel() < DEFAULT_DOME_LEVEL) {
            return false;
        }

        return canInteract(turn, cell);
    }

    @Override
    public void doBuildDome(Turn turn, Cell cell) {
        turn.addDomePlaced(cell);
        cell.setDoomed(true);
    }

    private boolean hasMoved(Turn turn) {
        return turn.getMoves().size() > 0;
    }

    private boolean hasBuilt(Turn turn) {
        return turn.getBlocksPlaced().size() > 0 || turn.getDomesPlaced().size() > 0;
    }

    private boolean canBuild(Turn turn) {
        if (!hasMoved(turn)) {
            return false;
        }

        return !hasBuilt(turn);
    }

    private boolean canInteract(Turn turn, Cell cell) {
        if (turn.getWorker().getCell().equals(cell)) {
            return false;
        }

        if (!turn.isNeighbour(cell)) {
            return false;
        }

        for (Worker other : turn.getOtherWorkers()) {
            if (other.getCell().equals(cell)) {
                return false;
            }
        }

        return true;
    }

}
