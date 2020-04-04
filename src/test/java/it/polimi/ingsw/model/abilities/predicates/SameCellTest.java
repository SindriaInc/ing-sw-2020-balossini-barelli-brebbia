package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SameCellTest {

    private SameCell sameCell;
    private Turn turn;
    private Worker worker;
    private Cell cell1;
    private Cell cell2;

    @BeforeEach
    void setUp() {
        cell1 = new Cell(0, 0);
        cell2 = new Cell(0, 1);
        worker = new Worker(cell1);
        turn = new Turn(worker, new HashMap<>(), (cell) -> List.of());
        sameCell = new SameCell();
    }

    @Test
    void check() {
        assertTrue(sameCell.check(turn, worker, cell1));
        assertFalse(sameCell.check(turn, worker, cell2));
    }

}