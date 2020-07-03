package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.CanInteractNoWorkers;

import java.util.List;
import java.util.Optional;

/**
 * Decorator that allows a force move pushing the forced worker in another cell
 */
public class ForcePushMove extends AbstractForceMove {

    private final ITriPredicate canInteractNoWorkers;

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public ForcePushMove(IAbilities abilities) {
        super(abilities);

        canInteractNoWorkers = new CanInteractNoWorkers();
    }

    /**
     * @see AbstractForceMove#checkCanForceInMovePhase(Turn, Worker)
     */
    @Override
    public boolean checkCanForceInMovePhase(Turn turn, Worker forcedWorker) {
        Optional<Cell> destinationCell = findDestinationCell(turn, forcedWorker.getCell());

        if (destinationCell.isEmpty()) {
            return false;
        }

        return canInteractNoWorkers.check(turn, forcedWorker, destinationCell.get());
    }

    /**
     * @see AbstractForceMove#doForceInMovePhase(Turn, Worker)
     */
    @Override
    public void doForceInMovePhase(Turn turn, Worker forcedWorker) {
        Optional<Cell> destination = findDestinationCell(turn, forcedWorker.getCell());

        if (destination.isEmpty()) {
            throw new IllegalArgumentException("No destination cell available");
        }

        forcedWorker.force(destination.get());
        turn.addMovedWorker(forcedWorker);
    }

    /**
     * Finds the destination cell where the opponent's worker will be pushed
     * @param turn The current turn
     * @param cell The shifter's cell
     * @return The destination cell
     */
    private Optional<Cell> findDestinationCell(Turn turn, Cell cell) {
        Cell startCell = turn.getWorker().getCell();
        int destinationX = 2 * cell.getX() - startCell.getX();
        int destinationY = 2 * cell.getY() - startCell.getY();

        List<Cell> cellNeighbors = turn.getNeighbours(cell);
        for (Cell destinationCell : cellNeighbors) {
            if (destinationCell.getX() == destinationX && destinationCell.getY() == destinationY) {
                return Optional.of(destinationCell);
            }
        }

        return Optional.empty();
    }

}
