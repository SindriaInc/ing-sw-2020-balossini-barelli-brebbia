package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.MovePhase;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_MOVES;

/**
 * Decorator that allows to force a neighboring opponent worker to the space directly
 * on the other side of the player's worker, if that space is unoccupied
 */
public class ParkourCross extends AbilitiesDecorator {

    private final ITriPredicate movePhase;

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public ParkourCross(IAbilities abilities) {
        super(abilities);

        movePhase = new MovePhase(DEFAULT_MAX_MOVES);
    }

    /**
     * @see AbilitiesDecorator#checkCanForce(Turn, Worker, Cell)
     */
    @Override
    public boolean checkCanForce(Turn turn, Worker worker, Cell cell) {
        if (!movePhase.check(turn, cell)) {
            return super.checkCanForce(turn, worker, cell);
        }

        if (turn.getForces().size() > 0) {
            return super.checkCanForce(turn, worker, cell);
        }

        if (cell.isDoomed()) {
            return super.checkCanForce(turn, worker, cell);
        }

        Cell forcedWorkerCell = worker.getCell();
        Cell workerCell = turn.getWorker().getCell();

        for (Worker other : turn.getOtherWorkers()) {
            if (cell.equals(other.getCell())) {
                return super.checkCanForce(turn, worker, cell);
            }
        }

        boolean check = turn.getNeighbours(workerCell).contains(cell) &&                // cell near to worker
                turn.getNeighbours(workerCell).contains(forcedWorkerCell) &&            // forcedWorker near to worker
                2 * workerCell.getX() - forcedWorkerCell.getX() - cell.getX() == 0 &&   // cell and forcedWorker opposite on X axis
                2 * workerCell.getY() - forcedWorkerCell.getY() - cell.getY() == 0 &&   // cell and forcedWorker opposite on Y axis
                !turn.hasSamePlayer(worker);                                            // worker and forcedWorker have different players

        return check || super.checkCanForce(turn, worker, cell);
    }

}
