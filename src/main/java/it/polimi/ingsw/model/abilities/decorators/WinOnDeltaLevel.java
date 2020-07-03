package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.OpponentAbilitiesDecorator;

import java.util.Optional;

/**
 * Decorator that allows the victory on a certain difference between levels
 */
public class WinOnDeltaLevel extends AbilitiesDecorator {

    protected static final int WIN_DELTA = -2;

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public WinOnDeltaLevel(IAbilities abilities) {
        super(abilities);
    }

    /**
     * @see AbilitiesDecorator#checkHasWon(Turn) 
     */
    @Override
    public boolean checkHasWon(Turn turn) {
        for (Worker worker : turn.getCandidateWinWorkers()) {
            Optional<Integer> difference = worker.getLastMovementLevelDifference();

            if (difference.isPresent() && difference.get() <= WIN_DELTA) {
                return true;
            }
        }

        return super.checkHasWon(turn);
    }

}
