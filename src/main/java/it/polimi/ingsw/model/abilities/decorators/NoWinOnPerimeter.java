package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.OpponentAbilitiesDecorator;

import java.util.List;
import java.util.Optional;
import static it.polimi.ingsw.model.Game.*;

public class NoWinOnPerimeter extends OpponentAbilitiesDecorator {

    public NoWinOnPerimeter(IAbilities abilities, List<Worker> workers) {
        super(abilities, workers);
    }

    @Override
    public boolean checkHasWon(List<Worker> workers) {
        for (Worker worker : workers) {
            Cell cell=worker.getCell();
            if (cell.getX()==0 || cell.getX()==BOARD_COLUMNS-1 || cell.getY()==0 || cell.getY()==BOARD_ROWS-1) {
                return false;
            }
        }

        return super.checkHasWon(workers);
    }

}