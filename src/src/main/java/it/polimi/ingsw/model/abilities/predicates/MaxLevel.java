package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.ITriPredicate;

/**
 * Predicate used to check if a cell level is the max level
 */
public class MaxLevel implements ITriPredicate {

    private final int maxLevel;

    /**
     * Class constructor
     * @param maxLevel The max level of the board
     */
    public MaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    /**
     * Returns true if the Cell level is equal to 'maxLevel'
     * @see ITriPredicate#check(Turn, Worker, Cell)
     */
    @Override
    public boolean check(Turn turn, Worker worker, Cell cell) {
        return cell.getLevel() >= maxLevel;
    }

}
