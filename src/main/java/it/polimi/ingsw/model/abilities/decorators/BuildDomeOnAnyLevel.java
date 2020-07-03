package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.BuildPhase;
import it.polimi.ingsw.model.abilities.predicates.CanInteractNoWorkers;

/**
 * Decorator that allows to built a dome on any level
 */
public class BuildDomeOnAnyLevel extends AbilitiesDecorator {

    private final ITriPredicate buildPhase;
    private final ITriPredicate canInteractNoWorkers;

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public BuildDomeOnAnyLevel(IAbilities abilities) {
        super(abilities);

        buildPhase = new BuildPhase();
        canInteractNoWorkers = new CanInteractNoWorkers();
    }

    /**
     * @see AbilitiesDecorator#checkCanBuildDome(Turn, Cell)
     */
    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        if (!buildPhase.check(turn, cell)) {
            return super.checkCanBuildDome(turn, cell);
        }

        return canInteractNoWorkers.check(turn, cell) || super.checkCanBuildDome(turn, cell);
    }

}
