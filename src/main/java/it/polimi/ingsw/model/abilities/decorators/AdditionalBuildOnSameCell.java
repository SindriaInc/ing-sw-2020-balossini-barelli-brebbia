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
import static it.polimi.ingsw.model.abilities.DefaultAbilities.*;

public class AdditionalBuildOnSameCell extends AbilitiesDecorator {

    private static final int BUILDS = 2;

    private ITriPredicate buildPhase;
    private ITriPredicate canInteractNoWorkers;

    public AdditionalBuildOnSameCell(IAbilities abilities) {
        super(abilities);

        buildPhase = new BuildPhase(BUILDS);
        canInteractNoWorkers = new CanInteractNoWorkers();
    }

    @Override
    public boolean checkCanBuildBlock(Turn turn, Cell cell) {

//        if (!buildPhase.check(turn, cell)) {
//            return super.checkCanBuildBlock(turn, cell);
//        }
//        if (turn.getBlocksPlaced().contains(cell) && turn.getBlocksPlaced().size()==1)
//            return true;
        if (turn.getDomesPlaced().size()>0 || cell.getLevel()>=DEFAULT_WIN_LEVEL || !turn.getBlocksPlaced().contains(cell))
            return false;

        return super.checkCanBuildBlock(turn, cell);
    }

    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        boolean check=false;

//        if (!buildPhase.check(turn, cell)) {
//            return super.checkCanBuildBlock(turn, cell);
//        }
        if (turn.getBlocksPlaced().contains(cell) && turn.getBlocksPlaced().size()==1)
            return true;
        if (turn.getDomesPlaced().size()>0 || cell.getLevel()>=DEFAULT_WIN_LEVEL)
            return false;

        return super.checkCanBuildDome(turn, cell);
    }
}
