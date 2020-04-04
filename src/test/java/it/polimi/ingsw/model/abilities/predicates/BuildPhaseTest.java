package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildPhaseTest {

    private BuildPhase buildPhase;
    private Turn turn;
    private Cell cell;

    @BeforeEach
    void setUp() {
        cell = new Cell(0, 0);
        turn = new Turn(new Worker(cell), new HashMap<>(), (cell) -> List.of());
        buildPhase = new BuildPhase();
    }

    @Test
    void checkWithBlock() {
        assertFalse(buildPhase.check(turn, cell));

        turn.addMovement(cell);

        assertTrue(buildPhase.check(turn, cell));

        turn.addBlockPlaced(cell);

        assertFalse(buildPhase.check(turn, cell));
    }

    @Test
    void checkWithDome() {
        assertFalse(buildPhase.check(turn, cell));

        turn.addMovement(cell);

        assertTrue(buildPhase.check(turn, cell));

        turn.addDomePlaced(cell);

        assertFalse(buildPhase.check(turn, cell));
    }

}