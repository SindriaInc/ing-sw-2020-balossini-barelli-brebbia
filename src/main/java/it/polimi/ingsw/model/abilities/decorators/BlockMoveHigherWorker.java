package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.OpponentAbilitiesDecorator;

import java.util.List;

/**
 * Decorator that blocks an the opponent highest worker
 */
public class BlockMoveHigherWorker extends OpponentAbilitiesDecorator {

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public BlockMoveHigherWorker(IAbilities abilities, List<Worker> workers) {
        super(abilities, workers);
    }

    /**
     * @see OpponentAbilitiesDecorator#checkCanMove(Turn, Cell)
     */
    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        for (Worker worker : turn.getOtherWorkers()) {
            if (turn.hasSamePlayer(worker) && turn.getWorker().getCell().getLevel() > worker.getCell().getLevel()) {
                return false;
            }
        }

        return super.checkCanMove(turn, cell);
    }
}
