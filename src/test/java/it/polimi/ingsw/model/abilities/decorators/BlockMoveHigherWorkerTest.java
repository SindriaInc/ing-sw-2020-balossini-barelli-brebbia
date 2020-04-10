package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.TestConstants;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.DefaultAbilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BlockMoveHigherWorkerTest {

    private Board board;
    private BlockMoveHigherWorker blockMoveHigherWorker;
    private Turn turn;
    Worker enemyWorker;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker1 = new Worker(board.getCellFromCoords(0, 0));
        Worker worker2 = new Worker(board.getCellFromCoords(1, 0));
        enemyWorker = new Worker(board.getCellFromCoords(2, 2));

        worker2.getCell().setLevel(1);


        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, true);
        otherWorkers.put(enemyWorker, false);

        blockMoveHigherWorker = new BlockMoveHigherWorker(new DefaultAbilities(), List.of(enemyWorker));
        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
    }

    /**
     * Check lower worker can move
     */
    @Test
    void checkAllowMoveLower() {
        assertTrue(blockMoveHigherWorker.checkCanMove(turn, board.getCellFromCoords(0, 1)));
    }

    /**
     * Check higher worker can't move
     */
    @Test
    void checkNoMoveHigher() {
        board.getCellFromCoords(0, 0).setLevel(2);
        assertFalse(blockMoveHigherWorker.checkCanMove(turn, board.getCellFromCoords(0, 1)));
    }
}