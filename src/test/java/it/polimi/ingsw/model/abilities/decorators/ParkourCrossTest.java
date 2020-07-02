package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.abilities.DefaultAbilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParkourCrossTest {

    private Board board;
    private ParkourCross abilities;
    private Turn turn;
    private Worker worker2;
    private Worker worker4;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker1 = new Worker(0, board.getCellFromCoords(1, 1));
        worker2 = new Worker(1, board.getCellFromCoords(0, 0));
        Worker worker3 = new Worker(2, board.getCellFromCoords(2, 2));
        worker4 = new Worker(3, board.getCellFromCoords(1, 0));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);
        otherWorkers.put(worker3, false);
        otherWorkers.put(worker4, false);

        abilities = new ParkourCross(new DefaultAbilities());
        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
    }

    /**
     * Check that it's not possible to force after moving
     */
    @Test
    void checkNoCrossAfterMoving() {
        abilities.doMove(turn, board.getCellFromCoords(1, 1)); // Stay in the same cell
        assertFalse(abilities.checkCanForce(turn, worker4, board.getCellFromCoords(1, 2)));
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
     * Check that the worker can't force other workers in occupied cells
     */
    @Test
    void checkCannotForceDoomed() {
        Cell destination = board.getCellFromCoords(1, 2);

        destination.setDoomed(true);
        assertFalse(abilities.checkCanForce(turn, worker4, destination));
    }

    /**
     * Check that the worker can force other workers in opposite cells
     */
    @Test
    void checkCanForceOpposite() {
        assertTrue(abilities.checkCanForce(turn, worker4, board.getCellFromCoords(1, 2)));
    }

    /**
     * Check that the worker can't force two times
     */
    @Test
    void checkNoMoreThanOneOpposite() {
        abilities.doForce(turn, worker4, board.getCellFromCoords(1, 2));
        assertFalse(abilities.checkCanForce(turn, worker4, board.getCellFromCoords(1, 0)));
    }

}