package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.IUseStrategy;
import it.polimi.ingsw.model.abilities.strategies.DefaultUse;

import java.util.List;

public class ForcePushMove extends AbilitiesDecorator {

    private IUseStrategy useStrategy;

    public ForcePushMove(IAbilities abilities) {
        super(abilities);
        useStrategy = new DefaultUse();
    }

    private Cell findDestinationCell (Turn turn, Cell cell) {
        Cell startCell = turn.getWorker().getCell();
        int destinationX = 2*cell.getX() - startCell.getX();
        int destinationY = 2*cell.getY() - startCell.getY();

        List<Cell> cellNeighbors = turn.getNeighbours(cell);
        for (Cell destinationCell : cellNeighbors) {
            if (destinationCell.getX() == destinationX && destinationCell.getY() == destinationY) {
                return destinationCell;
            }
        }
        return null;
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        boolean check = false;
        Cell startCell = turn.getWorker().getCell();

        Cell destinationCell = findDestinationCell(turn, cell);
        if (destinationCell!=null)
            check = useStrategy.canInteractWorkersNotIncluded(turn, destinationCell);

        return check || super.checkCanMove(turn, cell);
    }

    @Override
    public void doMove(Turn turn, Cell cell) {
        for (Worker forcedWorker : turn.getOtherWorkers()) {
            if (forcedWorker.getCell()==cell) {
                forcedWorker.force(findDestinationCell(turn, cell));
                break;
            }
        }

        super.doMove(turn, cell);
    }
}
