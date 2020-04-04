package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.ITriPredicate;

public class CellLevelDifference implements ITriPredicate {

    private final int maxUp;

    public CellLevelDifference(int maxUp) {
        this.maxUp = maxUp;
    }

    /**
     * Returns true if the difference between the Worker's cell level and the Cell's level is greater than 'maxUp'
     * @see ITriPredicate#check(Turn, Worker, Cell)
     */
    @Override
    public boolean check(Turn turn, Worker worker, Cell cell) {
        return cell.getLevel() - worker.getCell().getLevel() <= maxUp;
    }

}
