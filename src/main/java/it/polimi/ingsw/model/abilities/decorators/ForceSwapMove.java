package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.DefaultAbilities;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.IUseStrategy;
import it.polimi.ingsw.model.abilities.strategies.DefaultUse;

public class ForceSwapMove extends AbilitiesDecorator {


    private IUseStrategy useStrategy;

    public ForceSwapMove(IAbilities abilities) {
        super(abilities);
        useStrategy = new DefaultUse();
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        boolean check;
        Worker forcedWorker = null;
        Worker worker = turn.getWorker();


        if(turn.getMoves().size() > 0){
            return false;
        }

        for(Worker i: turn.getOtherWorkers()){
            if (i.getCell().equals(cell)) {
                forcedWorker = i;
                break;
            }

        }
        if (forcedWorker != null && turn.hasSamePlayer(forcedWorker)) {
            return false;
        }


        if (cell.getLevel() - worker.getCell().getLevel() > DefaultAbilities.DEFAULT_MAX_UP) {
            return false;
        }

        check = useStrategy.canInteractWorkersIncluded(turn, cell);

        return check || super.checkCanMove(turn, cell);
    }

    @Override
    public void doMove(Turn turn, Cell cell) {

        for (Worker forcedWorker : turn.getOtherWorkers()) {
            if (forcedWorker.getCell().equals(cell)) {
                forcedWorker.force(turn.getWorker().getCell());
                break;
            }
        }
        super.doMove(turn, cell);

    }

}
