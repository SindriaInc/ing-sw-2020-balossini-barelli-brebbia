package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.BuildPhase;
import it.polimi.ingsw.model.abilities.predicates.MaxLevel;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_BUILD_LEVEL;

/**
 * Decorator that allows to build on the cell where the worker is present
 */
public class BuildBelow extends AbilitiesDecorator {

    private final ITriPredicate maxBuildLevel;
    private final ITriPredicate buildPhase;

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public BuildBelow(IAbilities abilities) {
        super(abilities);

        maxBuildLevel = new MaxLevel(DEFAULT_MAX_BUILD_LEVEL);
        buildPhase = new BuildPhase();
    }

    /**
     * @see AbilitiesDecorator#checkCanBuildBlock(Turn, Cell)
     */
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
