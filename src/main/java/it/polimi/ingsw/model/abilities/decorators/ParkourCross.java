package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

public class ParkourCross extends AbilitiesDecorator {

    public ParkourCross(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkCanForce(Turn turn, Worker worker, Cell cell) {
        boolean check;
        Cell forcedWorkerCell = worker.getCell();
        Cell workerCell = turn.getWorker().getCell();

        check = turn.getNeighbours(workerCell).contains(cell) &&                //cell near to worker
                turn.getNeighbours(workerCell).contains(forcedWorkerCell) &&    //forcedWorker near to worker
                2*workerCell.getX()-forcedWorkerCell.getX()-cell.getX()==0 &&   //cell and forcedWorker opposite on X axis
                2*workerCell.getY()-forcedWorkerCell.getY()-cell.getY()==0 &&   //cell and forcedWorker opposite on Y axis
                !turn.hasSamePlayer(worker);                                    //worker and forcedWorker have different players

        for (Worker others : turn.getOtherWorkers()) {
            if (cell.equals(others.getCell())) {
                check = false;
                break;
            }
        }

        return check || super.checkCanForce(turn, worker, cell);
    }
}
