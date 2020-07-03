package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.IAbilities;

/**
 * Decorator that allows an additional built but not on the board perimeter
 */
public class AdditionalBuildNotOnPerimeter extends AbstractAdditionalBuild {

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public AdditionalBuildNotOnPerimeter(IAbilities abilities) {
        super(abilities);
    }

    /**
     * @see AbstractAdditionalBuild#checkCanAdditionalBuild(Turn, Cell)
     */
    @Override
    public boolean checkCanAdditionalBuild(Turn turn, Cell cell) {
        return !turn.isPerimeterSpace(cell);
    }

}
