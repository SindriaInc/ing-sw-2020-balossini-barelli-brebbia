package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.OpponentAbilitiesDecorator;

import java.util.List;
import java.util.Optional;

public class BlockMoveHigherWorker extends OpponentAbilitiesDecorator {

    public BlockMoveHigherWorker(IAbilities abilities, List<Worker> workers) {
        super(abilities, workers);
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        for (Worker worker : turn.getOtherWorkers()) {
            if (turn.hasSamePlayer(worker) && turn.getWorker().getCell().getLevel()>worker.getCell().getLevel()) {
                return false;
            }
        }

        return super.checkCanMove(turn, cell);
    }
}
