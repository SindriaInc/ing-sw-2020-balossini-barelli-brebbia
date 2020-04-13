package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.OpponentAbilitiesDecorator;

import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_WIN_LEVEL;

public class NoWinOnPerimeter extends OpponentAbilitiesDecorator {

    public NoWinOnPerimeter(IAbilities abilities, List<Worker> workers) {
        super(abilities, workers);
    }

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