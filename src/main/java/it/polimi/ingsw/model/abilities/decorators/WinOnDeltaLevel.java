package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

public class WinOnDeltaLevel extends AbilitiesDecorator {

    private int minDelta = -2;

    public WinOnDeltaLevel(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkHasWon() {
        boolean check = false;
        // TODO: Implement additional check
        return check || super.checkHasWon();
    }

}
