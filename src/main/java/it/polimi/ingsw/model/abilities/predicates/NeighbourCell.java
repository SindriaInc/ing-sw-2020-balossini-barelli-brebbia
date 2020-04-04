package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.ITriPredicate;

public class NeighbourCell implements ITriPredicate {

    /**
     * Returns true if the Cell is one of the Worker's cell neighbour
     * @see ITriPredicate#check(Turn, Worker, Cell)
     */
    @Override
    public boolean check(Turn turn, Worker worker, Cell cell) {
        return turn.getNeighbours(worker.getCell()).contains(cell);
    }

}
