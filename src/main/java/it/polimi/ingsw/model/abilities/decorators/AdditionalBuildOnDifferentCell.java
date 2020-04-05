package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.BuildPhase;
import it.polimi.ingsw.model.abilities.predicates.CanInteractNoWorkers;
import it.polimi.ingsw.model.abilities.predicates.CellLevelDifference;
import it.polimi.ingsw.model.abilities.predicates.MovePhase;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_UP;

public class AdditionalBuildOnDifferentCell extends AbilitiesDecorator {

    private static final int BUILDS = 2;

    private ITriPredicate buildPhase;
    private ITriPredicate canInteractNoWorkers;

    public AdditionalBuildOnDifferentCell(IAbilities abilities) {
        super(abilities);

        buildPhase = new BuildPhase(BUILDS);
        canInteractNoWorkers = new CanInteractNoWorkers();
    }

    @Override
    public boolean checkCanBuildBlock(Turn turn, Cell cell) {
        boolean check;

//        if (!buildPhase.check(turn, cell)) {
//            return super.checkCanBuildBlock(turn, cell);
//        }
        if (!checkCanBuild(turn, cell))
            return false;

        return super.checkCanBuildBlock(turn, cell);
    }

    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        boolean check;

//        if (!buildPhase.check(turn, cell)) {
//            return super.checkCanBuildDome(turn, cell);
//        }
        if (!checkCanBuild(turn, cell))
            return false;;

        return super.checkCanBuildDome(turn, cell);
    }

    private boolean checkCanBuild(Turn turn, Cell cell) {
        boolean check;

        check = canInteractNoWorkers.check(turn, cell) &&
                !turn.getBlocksPlaced().contains(cell) &&
                !turn.getDomesPlaced().contains(cell);

        return check;
    }
}
