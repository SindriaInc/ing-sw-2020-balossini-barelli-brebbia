package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.IUseStrategy;
import it.polimi.ingsw.model.abilities.strategies.DefaultUse;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_UP;

public class AdditionalMove extends AbilitiesDecorator {

    private IUseStrategy useStrategy;

    public AdditionalMove(IAbilities abilities) {
        super(abilities);
        useStrategy = new DefaultUse();
    }



    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        Worker worker = turn.getWorker();
        boolean check;


        if (turn.getMoves().size() >= 2) {
            return false;
        }

        if (cell.getLevel() - worker.getCell().getLevel() > DEFAULT_MAX_UP) {
            return false;
        }

        check = useStrategy.canInteractWorkersNotIncluded(turn, cell);

        return check || super.checkCanMove(turn, cell);
    }

}
