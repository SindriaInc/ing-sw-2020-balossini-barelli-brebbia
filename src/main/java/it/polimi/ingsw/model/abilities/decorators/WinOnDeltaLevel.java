package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

import java.util.List;

public class WinOnDeltaLevel extends AbilitiesDecorator {

    private static final int WIN_DELTA = -2;

    public WinOnDeltaLevel(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkHasWon(List<Worker> workers) {
        for (Worker worker : workers) {
            if (worker.getCell().getLevel() - worker.getPreviousCell().getLevel() <= WIN_DELTA) {
                return true;
            }
        }

        return super.checkHasWon(workers);
    }

}
