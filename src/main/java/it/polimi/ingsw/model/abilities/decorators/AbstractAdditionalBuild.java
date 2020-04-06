package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.BuildPhase;
import it.polimi.ingsw.model.abilities.predicates.CanInteractNoWorkers;
import it.polimi.ingsw.model.abilities.predicates.MaxLevel;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_BUILD_LEVEL;

public abstract class AbstractAdditionalBuild extends AbilitiesDecorator {

    private static final int BUILDS = 2;

    private ITriPredicate buildPhase;
    private ITriPredicate canInteractNoWorkers;
    private ITriPredicate maxBuildLevel;

    public AbstractAdditionalBuild(IAbilities abilities) {
        super(abilities);

        buildPhase = new BuildPhase(BUILDS);
        canInteractNoWorkers = new CanInteractNoWorkers();
        maxBuildLevel = new MaxLevel(DEFAULT_MAX_BUILD_LEVEL);
    }

    /**
     * Check whether or not the Worker can build on the Cell, after having already built in another cell
     * @param turn The Turn
     * @param cell The Cell to build onto
     * @return true if the Worker can build on the Cell
     */
    public abstract boolean checkCanAdditionalBuild(Turn turn, Cell cell);

    @Override
    public boolean checkCanBuildBlock(Turn turn, Cell cell) {
        if (!buildPhase.check(turn, cell)) {
            return super.checkCanBuildBlock(turn, cell);
        }

        if (maxBuildLevel.check(turn, cell)) {
            return super.checkCanBuildBlock(turn, cell);
        }

        if (turn.getBuilds().size() > 0 && !checkCanAdditionalBuild(turn, cell)) {
            return super.checkCanBuildBlock(turn, cell);
        }

        return canInteractNoWorkers.check(turn, cell) || super.checkCanBuildBlock(turn, cell);
    }

    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        if (!buildPhase.check(turn, cell)) {
            return super.checkCanBuildDome(turn, cell);
        }

        if (!maxBuildLevel.check(turn, cell)) {
            return super.checkCanBuildDome(turn, cell);
        }

        if (turn.getBuilds().size() > 0 && !checkCanAdditionalBuild(turn, cell)) {
            return super.checkCanBuildDome(turn, cell);
        }

        return canInteractNoWorkers.check(turn, cell) || super.checkCanBuildDome(turn, cell);
    }

}
