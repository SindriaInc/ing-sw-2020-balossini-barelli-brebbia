package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.CanInteract;
import it.polimi.ingsw.model.abilities.predicates.CellLevelDifference;
import it.polimi.ingsw.model.abilities.predicates.MovePhase;

import java.util.Optional;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_MOVES;
import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_UP;

/**
 * Abstract class for the decorators that allow a force move
 */
public abstract class AbstractForceMove extends AbilitiesDecorator {

    private final ITriPredicate movePhase;
    private final ITriPredicate cellLevelDifference;
    private final ITriPredicate canInteract;

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public AbstractForceMove(IAbilities abilities) {
        super(abilities);

        movePhase = new MovePhase(DEFAULT_MAX_MOVES);
        cellLevelDifference = new CellLevelDifference(DEFAULT_MAX_UP);
        canInteract = new CanInteract();
    }

    /**
     * Check whether or not the forced Worker can be forced to move
     * @param turn The Turn
     * @param forcedWorker The Worker to be forced
     * @return true if the Worker can be forced
     */
    public abstract boolean checkCanForceInMovePhase(Turn turn, Worker forcedWorker);

    /**
     * Forces the worker to move
     * @param turn The Turn
     * @param forcedWorker The Worker to be forced
     */
    public abstract void doForceInMovePhase(Turn turn, Worker forcedWorker);

    /**
     * Finds the Worker in the cell
     * @param turn The Turn
     * @param cell The Cell where the Worker should be
     * @return The Worker wrapped in {@link Optional}
     */
    public Optional<Worker> findForcedWorker(Turn turn, Cell cell) {
        for (Worker other : turn.getOtherWorkers()) {
            if (!turn.hasSamePlayer(other) && other.getCell().equals(cell)) {
                return Optional.of(other);
            }
        }

        return Optional.empty();
    }

    /**
     * @see AbilitiesDecorator#checkCanMove(Turn, Cell)
     */
    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        if (!movePhase.check(turn, cell)) {
            return super.checkCanMove(turn, cell);
        }

        if (!cellLevelDifference.check(turn, cell)) {
            return super.checkCanMove(turn, cell);
        }

        if (!canInteract.check(turn, cell)) {
            return super.checkCanMove(turn, cell);
        }

        Optional<Worker> forcedWorker = findForcedWorker(turn, cell);

        if (forcedWorker.isEmpty() || turn.hasSamePlayer(forcedWorker.get())) {
            return super.checkCanMove(turn, cell);
        }

        return checkCanForceInMovePhase(turn, forcedWorker.get()) || super.checkCanMove(turn, cell);
    }

    /**
     * @see AbilitiesDecorator#doMove(Turn, Cell)
     */
    @Override
    public void doMove(Turn turn, Cell cell) {
        Optional<Worker> forcedWorker = findForcedWorker(turn, cell);

        if (forcedWorker.isEmpty()) {
            super.doMove(turn, cell);
            return;
        }

        doForceInMovePhase(turn, forcedWorker.get());
        super.doMove(turn, cell);
    }

}
