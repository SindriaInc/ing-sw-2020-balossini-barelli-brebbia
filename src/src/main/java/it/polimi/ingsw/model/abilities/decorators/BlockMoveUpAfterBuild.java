package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.CellLevelDifference;

/**
 * Decorator that blocks the move action on an higher level after a built
 */
public class BlockMoveUpAfterBuild extends AbilitiesDecorator {

    private final ITriPredicate zeroLevelDifference;

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public BlockMoveUpAfterBuild(IAbilities abilities) {
        super(abilities);

        zeroLevelDifference = new CellLevelDifference(0);
    }

    /**
     * @see AbilitiesDecorator#checkCanMove(Turn, Cell)
     */
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