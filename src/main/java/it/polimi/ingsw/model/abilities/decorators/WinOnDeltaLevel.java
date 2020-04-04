package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

import java.util.List;

public class WinOnDeltaLevel extends AbilitiesDecorator {

    private int minDelta = -2;

    public WinOnDeltaLevel(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkHasWon(List<Worker> workers) {
        boolean check = false;
        for (Worker worker : workers) {
            if (worker.getCell().getLevel() - worker.getPreviousCell().getLevel() <= -2) {
                check = true;
                break;
            }
        }

        return check || super.checkHasWon(workers);
    }

}
