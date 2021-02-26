package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.IAbilities;

/**
 * Decorator that allows an additional built on the same cell
 */
public class AdditionalBuildOnSameCell extends AbstractAdditionalBuild {

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public AdditionalBuildOnSameCell(IAbilities abilities) {
        super(abilities);
    }

    /**
     * @see AbstractAdditionalBuild#checkCanAdditionalBuild(Turn, Cell)
     */
    @Override
    public boolean checkCanAdditionalBuild(Turn turn, Cell cell) {
        return turn.getBuilds().contains(cell);
    }

}
