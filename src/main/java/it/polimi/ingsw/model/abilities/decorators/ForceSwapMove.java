package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

public class ForceSwapMove extends AbilitiesDecorator {

    public ForceSwapMove(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkCanMove(Worker worker, Cell cell) {
        boolean check = false;
        // TODO: Implement additional check
        return check || super.checkCanMove(worker, cell);
    }

    @Override
    public void doMove(Worker worker, Cell cell) {
        super.doMove(worker, cell);
        // TODO: Implement additional effects
    }

}
