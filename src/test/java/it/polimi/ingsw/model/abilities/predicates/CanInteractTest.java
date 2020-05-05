package it.polimi.ingsw.model.abilities.predicates;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CanInteractTest {

    private CanInteract canInteract;
    private Turn turn;
    private Worker worker;
    private Cell cell1;
    private Cell cell2;
    private Cell cell3;

    @BeforeEach
    void setUp() {
        cell1 = new Cell(0, 0);
        cell2 = new Cell(0, 1);
        cell3 = new Cell(2, 0);
        worker = new Worker(0, cell1);

        turn = new Turn(worker, new HashMap<>(), (cell) -> {
            if (cell.equals(cell1)) {
                return List.of(cell2);
            }

            if (cell.equals(cell2)) {
                return List.of(cell1);
            }

            return List.of();
        }, cell -> false);
        canInteract = new CanInteract();
    }

    @Test
    void check() {
        assertFalse(canInteract.check(turn, cell1));
        assertTrue(canInteract.check(turn, cell2));
        assertFalse(canInteract.check(turn, cell3));

        cell2.setDoomed(true);

        assertFalse(canInteract.check(turn, cell2));
    }

}