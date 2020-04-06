package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.abilities.predicates.*;

import java.util.List;
import java.util.Optional;

public class DefaultAbilities implements IAbilities{

    public static final int DEFAULT_WIN_LEVEL = 3;
    public static final int DEFAULT_DOME_LEVEL = 3;
    public static final int DEFAULT_MAX_BUILD_LEVEL = 3;
    public static final int DEFAULT_MAX_MOVES = 1;
    public static final int DEFAULT_MAX_UP = 1;
    public static final int DEFAULT_MAX_BUILDS = 1;

    private ITriPredicate movePhase;
    private ITriPredicate buildPhase;
    private ITriPredicate maxBuildLevel;
    private ITriPredicate canInteractNoWorkers;
    private ITriPredicate cellLevelDifference;

    public DefaultAbilities() {
        movePhase = new MovePhase(DEFAULT_MAX_MOVES);
        buildPhase = new BuildPhase();
        maxBuildLevel = new MaxLevel(DEFAULT_MAX_BUILD_LEVEL);
        canInteractNoWorkers = new CanInteractNoWorkers();
        cellLevelDifference = new CellLevelDifference(DEFAULT_MAX_UP);
    }

    @Override
    public boolean checkHasWon(List<Worker> workers) {
        for (Worker worker : workers) {
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

}
