package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {

    private Board board;
    private Worker worker;

    @BeforeEach
    void setUp() {
        board = new Board();
        worker = new Worker(board.getCellFromCoords(0, 0));
    }

    @Test
    void testGetters() {
        assertEquals(worker.getCell(), getCell(0, 0));
    }

    @Test
    void checkMoveUp() {
        board.getCellFromCoords(1, 0).setLevel(1);

        worker.move(getCell(1, 0));
        assertTrue(worker::hasMovedUp);
    }

    @Test
    void checkNoMoveUp() {
        worker.move(getCell(1, 0));
        assertFalse(worker::hasMovedUp);
        worker.move(getCell(0, 0));
        assertFalse(worker::hasMovedUp);

        board.getCellFromCoords(0, 0).setLevel(1);

        worker.move(getCell(1, 0));
        assertFalse(worker::hasMovedUp);
    }

    @Test
    void doMove() {
        Cell cell = getCell(1, 0);

        worker.move(cell);
        assertEquals(worker.getCell(), cell);
    }

    private Cell getCell(int x, int y) {
        return board.getCellFromCoords(x, y);
    }

}