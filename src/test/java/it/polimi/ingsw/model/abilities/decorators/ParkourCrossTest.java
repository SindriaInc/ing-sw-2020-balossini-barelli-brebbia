package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.TestConstants;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.DefaultAbilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParkourCrossTest {

    private Board board;
    private ParkourCross abilities;
    private Turn turn;
    private Worker worker1, worker2, worker3, worker4;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        worker1 = new Worker(board.getCellFromCoords(1, 1));
        worker2 = new Worker(board.getCellFromCoords(0, 0));
        worker3 = new Worker(board.getCellFromCoords(2, 2));
        worker4 = new Worker(board.getCellFromCoords(1, 0));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);
        otherWorkers.put(worker3, false);
        otherWorkers.put(worker4, false);

        abilities = new ParkourCross(new DefaultAbilities());
        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell));
    }

    /**
     * Check that the worker can't force other workers in non opposite cells
     */
    @Test
    void checkCannotForceNonOpposite() {
        assertFalse(abilities.checkCanForce(turn, worker2, board.getCellFromCoords(1, 0)));
    }

    /**
     * Check that the worker can't force other workers in occupied cells
     */
    @Test
    void checkCannotForceOccupied() {
        assertFalse(abilities.checkCanForce(turn, worker2, board.getCellFromCoords(2, 2)));
    }

    /**
     * Check that the worker can force other workers in opposite cells
     */
    @Test
    void checkCanForceOpposite() {
        assertTrue(abilities.checkCanForce(turn, worker4, board.getCellFromCoords(1, 2)));
    }
}