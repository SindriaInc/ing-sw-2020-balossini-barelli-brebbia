package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.IUseStrategy;
import it.polimi.ingsw.model.abilities.strategies.DefaultUse;

public class BuildDomeOnAnyLevel extends AbilitiesDecorator {

    private IUseStrategy useStrategy;

    public BuildDomeOnAnyLevel(IAbilities abilities) {
        super(abilities);
        useStrategy = new DefaultUse();
    }

    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        boolean check = useStrategy.canInteractWorkersNotIncluded(turn, cell);

        return check || super.checkCanBuildDome(turn, cell);
    }

}
