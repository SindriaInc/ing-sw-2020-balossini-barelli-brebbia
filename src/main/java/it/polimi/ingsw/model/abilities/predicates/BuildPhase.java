package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.ITriPredicate;

public class BuildPhase implements ITriPredicate {

    /**
     * Returns true if the Turn is in the build phase
     * The build phase begins after the Worker has moved at least once during the Turn
     * The build phase ends when at least one block or dome is placed
     * @see ITriPredicate#check(Turn, Worker, Cell)
     */
    @Override
    public boolean check(Turn turn, Worker worker, Cell cell) {
        if (turn.getMoves().size() <= 0) {
            return false;
        }

        return turn.getBlocksPlaced().size() <= 0 && turn.getDomesPlaced().size() <= 0;
    }

}
