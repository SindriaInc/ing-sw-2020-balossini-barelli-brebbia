package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

public class BuildDomeOnAnyLevel extends AbilitiesDecorator {

    public BuildDomeOnAnyLevel(IAbilities abilities) {
        super(abilities);
    }

    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        boolean check = false;
        // TODO: Implement additional check
        return check || super.checkCanBuildDome(turn, cell);
    }

}
