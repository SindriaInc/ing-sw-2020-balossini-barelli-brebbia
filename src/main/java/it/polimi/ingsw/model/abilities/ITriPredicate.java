package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

/**
 * Functional interface for the conditions check
 */
@FunctionalInterface
public interface ITriPredicate {

    /**
     * Check a condition that involves a Turn, a Worker and a Cell
     * @param turn The Turn
     * @param worker The Worker
     * @param cell The Cell
     * @return true if the condition is met
     */
    boolean check(Turn turn, Worker worker, Cell cell);

    /**
     * Calls ITriPredicate#check(Turn, Worker, Cell) using Turn#getWorker as the Worker
     * @see ITriPredicate#check(Turn, Worker, Cell)
     */
    default boolean check(Turn turn, Cell cell) {
        return check(turn, turn.getWorker(), cell);
    }

}
