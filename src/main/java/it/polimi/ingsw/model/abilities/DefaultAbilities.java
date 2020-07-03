package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.predicates.*;

import java.util.Optional;

/**
 * The class representing the abilities of a player that hasn't got any god effect.
 * This class is used in a simple game.
 */
public class DefaultAbilities implements IAbilities{

    public static final int DEFAULT_WIN_LEVEL = 3;
    public static final int DEFAULT_DOME_LEVEL = 3;
    public static final int DEFAULT_MAX_BUILD_LEVEL = 3;
    public static final int DEFAULT_MAX_MOVES = 1;
    public static final int DEFAULT_MAX_UP = 1;
    public static final int DEFAULT_MAX_BUILDS = 1;

    private final ITriPredicate movePhase;
    private final ITriPredicate buildPhase;
    private final ITriPredicate maxBuildLevel;
    private final ITriPredicate canInteractNoWorkers;
    private final ITriPredicate cellLevelDifference;

    public DefaultAbilities() {
        movePhase = new MovePhase(DEFAULT_MAX_MOVES);
        buildPhase = new BuildPhase();
        maxBuildLevel = new MaxLevel(DEFAULT_MAX_BUILD_LEVEL);
        canInteractNoWorkers = new CanInteractNoWorkers();
        cellLevelDifference = new CellLevelDifference(DEFAULT_MAX_UP);
    }

    @Override
    public boolean checkHasWon(Turn turn) {
        for (Worker worker : turn.getCandidateWinWorkers()) {
            Optional<Integer> difference = worker.getLastMovementLevelDifference();

            if (difference.isEmpty() || difference.get() <= 0) {
                continue;
            }

            Optional<Cell> cell = worker.getLastMovementCell();

            if (cell.isPresent() && cell.get().getLevel() >= DEFAULT_WIN_LEVEL) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        if (!movePhase.check(turn, cell)) {
            return false;
        }

        if (!cellLevelDifference.check(turn, cell)) {
            return false;
        }

        return canInteractNoWorkers.check(turn, cell);
    }

    @Override
    public void doMove(Turn turn, Cell cell) {
        turn.addMovement(cell);
        turn.getWorker().move(cell);
    }

    @Override
    public boolean checkCanBuildBlock(Turn turn, Cell cell) {
        if (!buildPhase.check(turn, cell)) {
            return false;
        }

        if (maxBuildLevel.check(turn, cell)) {
            return false;
        }

        return canInteractNoWorkers.check(turn, cell);
    }

    @Override
    public void doBuildBlock(Turn turn, Cell cell) {
        turn.addBlockPlaced(cell);
        cell.setLevel(cell.getLevel() + 1);
    }

    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        if (!buildPhase.check(turn, cell)) {
            return false;
        }

        if (!maxBuildLevel.check(turn, cell)) {
            return false;
        }

        return canInteractNoWorkers.check(turn, cell);
    }

    @Override
    public void doBuildDome(Turn turn, Cell cell) {
        turn.addDomePlaced(cell);
        cell.setDoomed(true);
    }

    @Override
    public boolean checkCanForce(Turn turn, Worker target, Cell cell) {
        return false;
    }

    @Override
    public void doForce(Turn turn, Worker target, Cell cell) {
        turn.addForce(target, cell);
        target.force(cell);
    }

}
