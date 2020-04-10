package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CanInteractNoWorkersTest {

    private CanInteractNoWorkers canInteractNoWorkers;
    private Turn turn;
    private Worker worker;
    private Cell cell1;
    private Cell cell2;
    private Cell cell3;
    private Cell cell4;

    @BeforeEach
    void setUp() {
        cell1 = new Cell(0, 0);
        cell2 = new Cell(0, 1);
        cell3 = new Cell(1, 1);
        cell4 = new Cell(3, 0);
        worker = new Worker(cell1);

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(new Worker(cell3), false);

        turn = new Turn(worker, otherWorkers, (cell) -> {
            if (cell.equals(cell1)) {
                return List.of(cell2, cell3);
            }

            if (cell.equals(cell2)) {
                return List.of(cell1, cell3);
            }

            if (cell.equals(cell3)) {
                return List.of(cell1, cell2);
            }

            return List.of();
        }, cell -> false);
        canInteractNoWorkers = new CanInteractNoWorkers();
    }

    @Test
    void check() {
        assertFalse(canInteractNoWorkers.check(turn, cell1));
        assertTrue(canInteractNoWorkers.check(turn, cell2));
        assertFalse(canInteractNoWorkers.check(turn, cell3));
        assertFalse(canInteractNoWorkers.check(turn, cell4));

        cell2.setDoomed(true);

        assertFalse(canInteractNoWorkers.check(turn, cell2));
    }

}