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

class MaxLevelTest {

    private static final int BUILD_MAX_LEVEL = 3;

    private MaxLevel maxLevel;
    private Turn turn;
    private Cell cell;

    @BeforeEach
    void setUp() {
        cell = new Cell(0, 0);
        turn = new Turn(new Worker(0, cell), new HashMap<>(), (cell) -> List.of(), cell -> false);
        maxLevel = new MaxLevel(BUILD_MAX_LEVEL);
    }

    @Test
    void check() {
        assertFalse(maxLevel.check(turn, cell));

        cell.setLevel(BUILD_MAX_LEVEL);

        assertTrue(maxLevel.check(turn, cell));
    }

}