package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.ITriPredicate;

public class CanInteractNoWorkers implements ITriPredicate {

    private ITriPredicate canInteract = new CanInteract();

    /**
     * Returns true if the Worker can interact with the Cell and there is no Worker in the cell
     * @see CanInteract#check(Turn, Worker, Cell)
     */
    @Override
    public boolean check(Turn turn, Worker worker, Cell cell) {
        for (Worker other : turn.getOtherWorkers()) {
            if (other.getCell().equals(cell)) {
                return false;
            }
        }

        return canInteract.check(turn, worker, cell);
    }

}
