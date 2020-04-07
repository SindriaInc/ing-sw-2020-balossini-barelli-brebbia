package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.OpponentAbilitiesDecorator;

import java.util.List;
import java.util.Optional;

public class BlockOnPlayerMoveUp extends OpponentAbilitiesDecorator {

    public BlockOnPlayerMoveUp(IAbilities abilities, List<Worker> workers) {
        super(abilities, workers);
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        if (cell.getLevel() <= turn.getWorker().getCell().getLevel()) {
            return super.checkCanMove(turn, cell);
        }

        for (Worker worker : super.getWorkers()) {
            Optional<Integer> difference = worker.getLastMovementLevelDifference();

            if (difference.isPresent() && difference.get() > 0) {
                return false;
            }
        }

        return super.checkCanMove(turn, cell);
    }

}
