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

class ForceSwapMoveTest {

    private Board board;
    private ForceSwapMove abilities;
    private Turn turn;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker1 = new Worker(0, board.getCellFromCoords(0, 0));
        Worker worker2 = new Worker(1, board.getCellFromCoords(1, 0));
        Worker worker3 = new Worker(2, board.getCellFromCoords(2, 0));
        Worker worker4 = new Worker(3, board.getCellFromCoords(0, 1));
        board.getCellFromCoords(0,1).setLevel(2);

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);
        otherWorkers.put(worker3, false);
        otherWorkers.put(worker4, false);

        abilities = new ForceSwapMove(new DefaultAbilities());
        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
    }

    /**
     * Check that the decorator doesn't affect a normal move with no worker to be forced
     */
    @Test
    void checkNoEffectMove() {
        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(1, 1)));
        abilities.doMove(turn, board.getCellFromCoords(0, 1));
        assertEquals(turn.getWorker().getCell(), board.getCellFromCoords(0, 1));
    }

    /**
     * Check that a worker with power can swap another opponent worker
     */
    @Test
    void checkCanSwap() {
        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(1, 0)));
    }

    /**
     * Check that a worker with this power can't swap another opponent worker if not in a neighbour cell
     */
    @Test
    void checkNoSwap() {
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(2, 0)));
    }

    /**
     * Check that a worker with this power can't swap another opponent worker if the levels difference is greater than one
     */
    @Test
    void checkNoSwapDiffLevel() {
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(0, 1)));
    }

    /**
     * Check that the worker does a correct force swap move
     */
    @Test
    void checkDoMove() {
        abilities.doMove(turn, board.getCellFromCoords(1, 0));
        assertEquals(turn.getWorker().getCell(), board.getCellFromCoords(1, 0));
        Worker forcedWorker = turn.getWorker();
        for (Worker other : turn.getOtherWorkers()) {
            if (other.getCell() == board.getCellFromCoords(0, 0)) {
                forcedWorker = other;
            }
        }

        assertEquals(forcedWorker.getCell(),board.getCellFromCoords(0,0));

        // Can't move anymore after having already moved
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(0, 0)));
    }

}