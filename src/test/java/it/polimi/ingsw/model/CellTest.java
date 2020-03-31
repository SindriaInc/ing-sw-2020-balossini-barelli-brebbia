package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    private Cell cell;

    @BeforeEach
    void setUp() {
        cell = new Cell(0, 0);
    }

    @Test
    void testGettersAndSetters() {
        cell.setLevel(1);
        cell.setDoomed(true);
        assertEquals(cell.getLevel(), 1);
        assertTrue(cell.isDoomed());
    }

}