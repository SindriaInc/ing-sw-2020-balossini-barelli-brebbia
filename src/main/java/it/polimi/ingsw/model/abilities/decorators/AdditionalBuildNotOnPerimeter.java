package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.IAbilities;

import static it.polimi.ingsw.model.Game.BOARD_COLUMNS;
import static it.polimi.ingsw.model.Game.BOARD_ROWS;

public class AdditionalBuildNotOnPerimeter extends AbstractAdditionalBuild{
    public AdditionalBuildNotOnPerimeter(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkCanAdditionalBuild(Turn turn, Cell cell) {
        return !(cell.getX()==0 || cell.getX()==BOARD_COLUMNS-1 || cell.getY()==0 || cell.getY()==BOARD_ROWS-1);
    }
}
