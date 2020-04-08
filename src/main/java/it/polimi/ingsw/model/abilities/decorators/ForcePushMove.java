package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.CanInteractNoWorkers;

import java.util.List;
import java.util.Optional;

public class ForcePushMove extends AbstractForceMove {

    private ITriPredicate canInteractNoWorkers;

    public ForcePushMove(IAbilities abilities) {
        super(abilities);

        canInteractNoWorkers = new CanInteractNoWorkers();
    }

    @Override
    public boolean checkCanForceInMovePhase(Turn turn, Worker forcedWorker) {
        Optional<Cell> destinationCell = findDestinationCell(turn, forcedWorker.getCell());

        if (destinationCell.isEmpty()) {
            return false;
        }

        return canInteractNoWorkers.check(turn, forcedWorker, destinationCell.get());
    }

    @Override
    public void doForceInMovePhase(Turn turn, Worker forcedWorker) {
        Optional<Cell> destination = findDestinationCell(turn, forcedWorker.getCell());

        if (destination.isEmpty()) {
            throw new IllegalArgumentException("No destination cell available");
        }

        forcedWorker.force(destination.get());
    }

    private Optional<Cell> findDestinationCell(Turn turn, Cell cell) {
        Cell startCell = turn.getWorker().getCell();
        int destinationX = 2*cell.getX() - startCell.getX();
        int destinationY = 2*cell.getY() - startCell.getY();

        List<Cell> cellNeighbors = turn.getNeighbours(cell);
        for (Cell destinationCell : cellNeighbors) {
            if (destinationCell.getX() == destinationX && destinationCell.getY() == destinationY) {
                return Optional.of(destinationCell);
            }
        }

        return Optional.empty();
    }

}
