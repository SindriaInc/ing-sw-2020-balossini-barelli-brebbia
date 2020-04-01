package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

public class ForcePushMove extends AbilitiesDecorator {

    public ForcePushMove(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        boolean check = false;
        // TODO: Implement additional check
        return check || super.checkCanMove(turn, cell);
    }

    @Override
    public void doMove(Turn turn, Cell cell) {
        super.doMove(turn, cell);
        // TODO: Implement additional effects
    }
}
