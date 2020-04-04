package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.BuildPhase;
import it.polimi.ingsw.model.abilities.predicates.CanInteractNoWorkers;

public class BuildDomeOnAnyLevel extends AbilitiesDecorator {

    private ITriPredicate buildPhase;
    private ITriPredicate canInteractNoWorkers;

    public BuildDomeOnAnyLevel(IAbilities abilities) {
        super(abilities);

        buildPhase = new BuildPhase();
        canInteractNoWorkers = new CanInteractNoWorkers();
    }

    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        if (!buildPhase.check(turn, cell)) {
            return super.checkCanBuildDome(turn, cell);
        }

        return canInteractNoWorkers.check(turn, cell) || super.checkCanBuildDome(turn, cell);
    }

}
