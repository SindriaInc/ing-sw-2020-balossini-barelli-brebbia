package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CellLevelDifferenceTest {

    private static final int MAX_UP = 1;

    private CellLevelDifference cellLevelDifference;
    private Cell cell1;
    private Cell cell2;
    private Cell cell3;
    private Turn turn;

    @BeforeEach
    void setUp() {
        cell1 = new Cell(0, 0);
        cell2 = new Cell(0, 1);
        cell3 = new Cell(0, 1);

        turn = new Turn(new Worker(cell1), new HashMap<>(), (cell) -> List.of());
        cellLevelDifference = new CellLevelDifference(MAX_UP);
    }

    @Test
    void check() {
        cell2.setLevel(MAX_UP);
        cell3.setLevel(MAX_UP + 1);

        assertTrue(cellLevelDifference.check(turn, cell1));
        assertTrue(cellLevelDifference.check(turn, cell2));
        assertFalse(cellLevelDifference.check(turn, cell3));
    }

}