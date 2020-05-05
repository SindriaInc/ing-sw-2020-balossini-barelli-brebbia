package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

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

    @Test
    void testOverrides() {
        // toString
        assertEquals(cell.toString(), "{x: 0, y: 0, doomed: false}");

        Cell cell2 = new Cell(0, 0);
        String string = "NotACell";
        // equals
        assertEquals(cell, cell2);
        assertNotEquals(cell, string);

        cell2 = cell;

        assertEquals(cell, cell2);

        // hashCode
        assertEquals(cell.hashCode(), Objects.hash(cell.getX(), cell.getY()));
    }

}