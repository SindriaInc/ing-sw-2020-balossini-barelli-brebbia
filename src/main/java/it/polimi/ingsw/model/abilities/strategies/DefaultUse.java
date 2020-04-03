package it.polimi.ingsw.model.abilities.strategies;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.abilities.IUseStrategy;

public class DefaultUse implements IUseStrategy {

    @Override
    public boolean canInteractWorkersIncluded(Turn turn, Cell cell) {
        if (turn.getWorker().getCell().equals(cell)) {
            return false;
        }

        if (!turn.getNeighbours(turn.getWorker().getCell()).contains(cell)) {
            return false;
        }


        if (cell.isDoomed()) {
            return false;
        }

        return true;
    }

    public boolean canInteractWorkersNotIncluded (Turn turn, Cell cell) {

        for (Worker other : turn.getOtherWorkers()) {
            if (other.getCell().equals(cell)) {
                return false;
            }
        }

        return canInteractWorkersIncluded(turn, cell);
    }
}
