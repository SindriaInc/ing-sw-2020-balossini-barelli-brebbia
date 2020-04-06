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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockOnPlayerMoveUpTest {

    private Board board;
    private BlockOnPlayerMoveUp blockOnPlayerMoveUp;
    private Turn turn;
    private Worker enemyWorker;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker = new Worker(board.getCellFromCoords(0, 0));
        enemyWorker = new Worker(board.getCellFromCoords(1, 0));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(enemyWorker, false);

        blockOnPlayerMoveUp = new BlockOnPlayerMoveUp(new DefaultAbilities(), List.of(enemyWorker));
        turn = new Turn(worker, otherWorkers, (cell) -> board.getNeighborings(cell));
    }

    @Test
    void checkAllowMoveUp() {
        enemyWorker.move(board.getCellFromCoords(1, 1)); // No up movement

        assertTrue(blockOnPlayerMoveUp.checkCanMove(turn, board.getCellFromCoords(0, 1)));
    }

    @Test
    void checkNoMoveUp() {
        board.getCellFromCoords(1, 1).setLevel(1);
        enemyWorker.move(board.getCellFromCoords(1, 1)); // Up movement

        board.getCellFromCoords(1, 0).setLevel(1); // Cell 1,0 is now empty but one level higher

        assertFalse(blockOnPlayerMoveUp.checkCanMove(turn, board.getCellFromCoords(1, 0)));
        assertTrue(blockOnPlayerMoveUp.checkCanMove(turn, board.getCellFromCoords(0, 1)));
    }

}