package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.IAbilities;

/**
 * Decorator that allows a swap betwenn the player worker and the forced worker
 */
public class ForceSwapMove extends AbstractForceMove {

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public ForceSwapMove(IAbilities abilities) {
        super(abilities);
    }

    /**
     * @see AbstractForceMove#checkCanForceInMovePhase(Turn, Worker)
     */
    @Override
    public boolean checkCanForceInMovePhase(Turn turn, Worker forcedWorker) {
        return true;
    }

    /**
     * @see AbstractForceMove#doForceInMovePhase(Turn, Worker)
     */
    @Override
    public void doForceInMovePhase(Turn turn, Worker forcedWorker) {
        forcedWorker.force(turn.getWorker().getCell());
        turn.addMovedWorker(forcedWorker);
    }

}
