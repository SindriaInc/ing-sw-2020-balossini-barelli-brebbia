package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.BuildPhase;
import it.polimi.ingsw.model.abilities.predicates.MaxLevel;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_BUILD_LEVEL;

public class BuildBelow extends AbilitiesDecorator {

    private final ITriPredicate maxBuildLevel;
    private final ITriPredicate buildPhase;

    public BuildBelow(IAbilities abilities) {
        super(abilities);

        maxBuildLevel = new MaxLevel(DEFAULT_MAX_BUILD_LEVEL);
        buildPhase = new BuildPhase();
    }

    @Override
    public boolean checkCanBuildBlock(Turn turn, Cell cell) {

        boolean check;

        check = !maxBuildLevel.check(turn, cell) &&
                cell.equals(turn.getWorker().getCell()) &&
                buildPhase.check(turn, cell) &&
                turn.getBuilds().size() < 1;

        return check || super.checkCanBuildBlock(turn, cell);
    }
}
