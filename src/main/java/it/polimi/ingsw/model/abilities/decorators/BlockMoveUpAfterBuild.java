package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.CellLevelDifference;

public class BlockMoveUpAfterBuild extends AbilitiesDecorator {

    private final ITriPredicate zeroLevelDifference;

    public BlockMoveUpAfterBuild(IAbilities abilities) {
        super(abilities);

        zeroLevelDifference = new CellLevelDifference(0);
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        if (turn.getBuilds().size() <= 0) {
            return super.checkCanMove(turn, cell);
        }

        if (!zeroLevelDifference.check(turn, cell)) {
            return false;
        }

        return super.checkCanMove(turn, cell);
    }

}