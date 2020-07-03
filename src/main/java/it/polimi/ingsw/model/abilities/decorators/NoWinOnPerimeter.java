package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.OpponentAbilitiesDecorator;

import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_WIN_LEVEL;

/**
 * Decorator that forbids the player's win if his winning worker is on a perimeter cell
 */
public class NoWinOnPerimeter extends OpponentAbilitiesDecorator {

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public NoWinOnPerimeter(IAbilities abilities, List<Worker> workers) {
        super(abilities, workers);
    }

    /**
     * @see OpponentAbilitiesDecorator#checkHasWon(Turn)
     */
    @Override
    public boolean checkHasWon(Turn turn) {
        for (Worker worker : turn.getCandidateWinWorkers()) {
            Optional<Integer> difference = worker.getLastMovementLevelDifference();

            if (difference.isEmpty() || difference.get() <= 0) {
                continue;
            }

            Optional<Cell> cell = worker.getLastMovementCell();

            if (cell.isPresent() && cell.get().getLevel() >= DEFAULT_WIN_LEVEL && turn.isPerimeterSpace(cell.get())) {
                turn.addBannedWinWorker(worker);
            }
        }

        return super.checkHasWon(turn);
    }

}