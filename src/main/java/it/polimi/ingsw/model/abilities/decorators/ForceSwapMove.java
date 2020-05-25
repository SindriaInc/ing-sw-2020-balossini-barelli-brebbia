package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.IAbilities;

public class ForceSwapMove extends AbstractForceMove {

    public ForceSwapMove(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkCanForceInMovePhase(Turn turn, Worker forcedWorker) {
        return true;
    }

    @Override
    public void doForceInMovePhase(Turn turn, Worker forcedWorker) {
        forcedWorker.force(turn.getWorker().getCell());
        turn.addMovedWorker(forcedWorker);
    }

}
