package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.ITriPredicate;

/**
 * Predicate used to check the possible interactions of a worker (other workers excluded)
 */
public class CanInteract implements ITriPredicate {

    private final ITriPredicate neighbourCell = new NeighbourCell();
    private final ITriPredicate sameCell = new SameCell();

    /**
     * Returns true if the Worker can interact with the Cell
     * Interaction is allowed only with neighbour cells that are not doomed and are not the one the Worker is standing on
     * @see NeighbourCell#check(Turn, Worker, Cell)
     * @see SameCell#check(Turn, Worker, Cell)
     * @see ITriPredicate#check(Turn, Worker, Cell)
     */
    @Override
    public boolean check(Turn turn, Worker worker, Cell cell) {
        if (sameCell.check(turn, worker, cell)) {
            return false;
        }

        if (!neighbourCell.check(turn, worker, cell)) {
            return false;
        }

        return !cell.isDoomed();
    }

}
