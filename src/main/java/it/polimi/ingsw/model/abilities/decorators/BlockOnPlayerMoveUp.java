package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

import java.util.List;

public class BlockOnPlayerMoveUp extends AbilitiesDecorator {

    /**
     * The workers that need to be checked to see whether or not we need to block the movement
     */
    private List<Worker> workersToCheck;

    public BlockOnPlayerMoveUp(IAbilities abilities, List<Worker> workersToCheck) {
        super(abilities);

        this.workersToCheck = workersToCheck;
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        boolean check = false;
        // TODO: Implement additional check
        return check && super.checkCanMove(turn, cell);
    }

    @Override
    public void doMove(Turn turn, Cell cell) {
        super.doMove(turn, cell);
        // TODO: Implement additional effects
    }

}
