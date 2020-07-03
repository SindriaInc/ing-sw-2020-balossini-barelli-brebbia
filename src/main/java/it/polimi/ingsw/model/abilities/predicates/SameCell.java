package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.ITriPredicate;

/**
 * Predicate used to check if a cell is the same cell one provided
 */
public class SameCell implements ITriPredicate {

    /**
     * Returns true if the Worker's cell is the same as the Cell provided
     * @see ITriPredicate#check(Turn, Worker, Cell)
     */
    @Override
    public boolean check(Turn turn, Worker worker, Cell cell) {
        return worker.getCell().equals(cell);
    }

}
