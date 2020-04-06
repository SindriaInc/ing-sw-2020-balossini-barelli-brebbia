package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

import java.util.List;
import java.util.Optional;

public class WinOnDeltaLevel extends AbilitiesDecorator {

    private static final int WIN_DELTA = -2;

    public WinOnDeltaLevel(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkHasWon(List<Worker> workers) {
        for (Worker worker : workers) {
            Optional<Integer> difference = worker.getLastMovementLevelDifference();

            if (difference.isPresent() && difference.get() <= WIN_DELTA) {
                return true;
            }
        }

        return super.checkHasWon(workers);
    }

}
