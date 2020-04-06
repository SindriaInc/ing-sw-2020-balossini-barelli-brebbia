package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

import java.util.List;
import java.util.Optional;

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
        if (cell.getLevel() <= turn.getWorker().getCell().getLevel()) {
            return super.checkCanMove(turn, cell);
        }

        for (Worker worker : workersToCheck) {
            Optional<Integer> difference = worker.getLastMovementLevelDifference();

            if (difference.isPresent() && difference.get() > 0) {
                return false;
            }
        }

        return super.checkCanMove(turn, cell);
    }

}
