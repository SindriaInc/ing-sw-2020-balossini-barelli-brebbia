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

class ForcePushMoveTest {

    private Board board;
    private ForcePushMove abilities;
    private Turn turn;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker1 = new Worker(board.getCellFromCoords(0, 0));
        Worker worker2 = new Worker(board.getCellFromCoords(1, 0));
        Worker worker3 = new Worker(board.getCellFromCoords(2, 0));
        Worker worker4 = new Worker(board.getCellFromCoords(1, 1));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);
        otherWorkers.put(worker3, false);
        otherWorkers.put(worker4, false);

        abilities = new ForcePushMove(new DefaultAbilities());
        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
    }

    /**
     * Check that the decorator doesn't affect a normal move with no worker to be forced
     */
    @Test
    void checkNoEffectMove() {
        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(0, 1)));
        abilities.doMove(turn, board.getCellFromCoords(0, 1));
        assertEquals(turn.getWorker().getCell(), board.getCellFromCoords(0, 1));
    }

    /**
     * Check that the worker can't push a worker out of the board
     */
    @Test
    void checkCannotForceWithNoDestination() {
        Worker worker1 = new Worker(board.getCellFromCoords(0, 1));
        Worker worker2 = new Worker(board.getCellFromCoords(0, 0));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);

        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(0, 0)));
        assertThrows(IllegalArgumentException.class, () -> abilities.doMove(turn, board.getCellFromCoords(0, 0)));
    }

    /**
     * Check that a worker can't move on a worker that can't be forced
     */
    @Test
    void checkCannotForceWithForcedCellOccupied() {
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(1, 0)));
    }

    /**
     * Check that a worker with this power can force a worker out of his cell
     */
    @Test
    void checkCanForcePush() {
        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(1, 1)));
    }

    /**
     * Check that the worker does a correct force push move
     */
    @Test
    void checkDoMove() {
        abilities.doMove(turn, board.getCellFromCoords(1, 1));
        assertEquals(turn.getWorker().getCell(), board.getCellFromCoords(1, 1));
        Worker forcedWorker = turn.getWorker();
        for (Worker other : turn.getOtherWorkers()) {
            if (other.getCell() == board.getCellFromCoords(2, 2)) {
                forcedWorker = other;
            }
        }

        assertEquals(forcedWorker.getCell(), board.getCellFromCoords(2, 2));

        // Can't move anymore after having already moved
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(0, 0)));
    }

}