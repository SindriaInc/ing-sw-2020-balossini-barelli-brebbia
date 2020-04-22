package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.CanInteractNoWorkers;
import it.polimi.ingsw.model.abilities.predicates.CellLevelDifference;
import it.polimi.ingsw.model.abilities.predicates.MaxLevel;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_BUILD_LEVEL;
import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_UP;

public class BuildBeforeMove extends AbilitiesDecorator {

    private final ITriPredicate canInteractNoWorkers;
    private final ITriPredicate maxBuildLevel;
    private final ITriPredicate cellLevelDifference;

    public BuildBeforeMove(IAbilities abilities) {
        super(abilities);

        canInteractNoWorkers = new CanInteractNoWorkers();
        maxBuildLevel = new MaxLevel(DEFAULT_MAX_BUILD_LEVEL);
        cellLevelDifference = new CellLevelDifference(DEFAULT_MAX_UP);
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        if (turn.getMoves().size() > 0) {
            return super.checkCanMove(turn, cell);
        }

        if (!cellLevelDifference.check(turn, cell)) {
            return super.checkCanMove(turn, cell);
        }

        return canInteractNoWorkers.check(turn, cell) || super.checkCanMove(turn, cell);
    }

    @Override
    public boolean checkCanBuildBlock(Turn turn, Cell cell) {
        if (turn.getBuilds().size() == 0 && turn.getMoves().size() == 1) {
            return super.checkCanBuildBlock(turn, cell);
        }

        if (maxBuildLevel.check(turn, cell)) {
            return super.checkCanBuildBlock(turn, cell);
        }

        if (turn.getStandardActions().size() == 2 && turn.getStandardActions().get(0).getType().isBuild() && turn.getStandardActions().get(1).getType() == Turn.ActionType.MOVEMENT) {
            return canInteractNoWorkers.check(turn, cell) || super.checkCanBuildBlock(turn, cell);
        }

        if (turn.getStandardActions().size() == 0) {
            return canInteractNoWorkers.check(turn, cell) || super.checkCanBuildBlock(turn, cell);
        }

        return super.checkCanBuildBlock(turn, cell);
    }

    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        if (turn.getBuilds().size() == 0 && turn.getMoves().size() == 1) {
            return super.checkCanBuildDome(turn, cell);
        }

        if (!maxBuildLevel.check(turn, cell)) {
            return super.checkCanBuildDome(turn, cell);
        }

        if (turn.getStandardActions().size() == 2 && turn.getStandardActions().get(0).getType().isBuild() && turn.getStandardActions().get(1).getType() == Turn.ActionType.MOVEMENT) {
            return canInteractNoWorkers.check(turn, cell) || super.checkCanBuildDome(turn, cell);
        }

        if (turn.getStandardActions().size() == 0) {
            return canInteractNoWorkers.check(turn, cell) || super.checkCanBuildDome(turn, cell);
        }

        return super.checkCanBuildDome(turn, cell);
    }

}