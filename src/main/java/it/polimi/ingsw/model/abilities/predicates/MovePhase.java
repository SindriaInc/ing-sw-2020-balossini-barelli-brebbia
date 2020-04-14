package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.ITriPredicate;

public class MovePhase implements ITriPredicate {

    /**
     * Max moves per move phase
     */
    private final int maxMoves;

    public MovePhase(int maxMoves) {
        this.maxMoves = maxMoves;
    }

    /**
     * Returns true if the Turn is in the move phase
     * The move phase begins at the start of the Turn
     * The move phase ends when the worker has moved 'maxMoves' times
     * @see ITriPredicate#check(Turn, Worker, Cell)
     */
    @Override
    public boolean check(Turn turn, Worker worker, Cell cell) {
        return turn.getMoves().size() < maxMoves;
    }

}
