package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovePhaseTest {

    private static final int MAX_MOVES = 2;

    private MovePhase movePhase;
    private Turn turn;
    private Cell cell;

    @BeforeEach
    void setUp() {
        cell = new Cell(0, 0);
        turn = new Turn(new Worker(0, cell), new HashMap<>(), (cell) -> List.of(), cell -> false);
        movePhase = new MovePhase(MAX_MOVES);
    }

    @Test
    void check() {
        assertTrue(movePhase.check(turn, cell));

        for (int i = 0; i < MAX_MOVES - 1; i++) {
            turn.addMovement(cell);
        }

        assertTrue(movePhase.check(turn, cell));

        for (int i = MAX_MOVES - 1; i < MAX_MOVES; i++) {
            turn.addMovement(cell);
        }

        assertFalse(movePhase.check(turn, cell));
    }

}