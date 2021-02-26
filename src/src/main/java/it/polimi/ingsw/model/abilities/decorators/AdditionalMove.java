package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.ITriPredicate;
import it.polimi.ingsw.model.abilities.predicates.CanInteractNoWorkers;
import it.polimi.ingsw.model.abilities.predicates.CellLevelDifference;
import it.polimi.ingsw.model.abilities.predicates.MovePhase;

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_UP;

/**
 * Decorator that allows an additional move
 */
public class AdditionalMove extends AbilitiesDecorator {

    private static final int MOVES = 2;

    private final ITriPredicate movePhase;
    private final ITriPredicate cellLevelDifference;
    private final ITriPredicate canInteractNoWorkers;

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public AdditionalMove(IAbilities abilities) {
        super(abilities);

        movePhase = new MovePhase(MOVES);
        cellLevelDifference = new CellLevelDifference(DEFAULT_MAX_UP);
        canInteractNoWorkers = new CanInteractNoWorkers();
    }

    /**
     * @see AbilitiesDecorator#checkCanMove(Turn, Cell)
     */
    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        if (!movePhase.check(turn, cell)) {
            return super.checkCanMove(turn, cell);
        }

        if (!cellLevelDifference.check(turn, cell)) {
            return super.checkCanMove(turn, cell);
        }

        if (turn.getMoves().size() > 0 && cell.equals(turn.getStartingCell())) {
            return super.checkCanMove(turn, cell);
        }

        return canInteractNoWorkers.check(turn, cell) || super.checkCanMove(turn, cell);
    }

}
