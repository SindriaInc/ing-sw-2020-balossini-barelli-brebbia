package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.IAbilities;

public class ForceSwapMove extends AbstractForceMove {

    public ForceSwapMove(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkCanForce(Turn turn, Worker forcedWorker) {
        return true;
    }

    @Override
    public void doForce(Turn turn, Worker forcedWorker) {
        forcedWorker.force(turn.getWorker().getCell());
    }

}
