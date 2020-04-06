package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.IAbilities;

public class AdditionalBuildOnDifferentCell extends AbstractAdditionalBuild {

    public AdditionalBuildOnDifferentCell(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkCanAdditionalBuild(Turn turn, Cell cell) {
        return !turn.getBuilds().contains(cell);
    }

}
