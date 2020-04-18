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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WinOnDeltaLevelTest {

    private Board board;
    private WinOnDeltaLevel abilities;
    private Turn turn;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker1 = new Worker(board.getCellFromCoords(0, 0));
        Worker worker2 = new Worker(board.getCellFromCoords(1, 0));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);

        abilities = new WinOnDeltaLevel(new DefaultAbilities());
        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
    }

    /**
     * Check that a worker with this power can win by moving down the delta level
     */
    @Test
    void checkDeltaWinCondition() {
        board.getCellFromCoords(0, 0).setLevel(-WinOnDeltaLevel.WIN_DELTA);
        board.getCellFromCoords(0, 1).setLevel(0);

        assertFalse(abilities.checkHasWon(turn));

        abilities.doMove(turn,  board.getCellFromCoords(0, 1));
        assertTrue(abilities.checkHasWon(turn));
    }

    /**
     * Check that a worker with this power can't win by moving down less than the delta level
     */
    @Test
    void checkNoDeltaWinCondition() {
        board.getCellFromCoords(0, 0).setLevel(1);
        board.getCellFromCoords(0, 1).setLevel(0);

        assertFalse(abilities.checkHasWon(turn));

        abilities.doMove(turn,  board.getCellFromCoords(0, 1));
        assertFalse(abilities.checkHasWon(turn));
    }

    /**
     * Check that a worker with this power can win with the default condition
     */
    @Test
    void checkDefaultWinCondition() {
        board.getCellFromCoords(0, 0).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL - 1);
        board.getCellFromCoords(0, 1).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL);

        assertFalse(abilities.checkHasWon(turn));

        abilities.doMove(turn,  board.getCellFromCoords(0, 1));
        assertTrue(abilities.checkHasWon(turn));
    }

}
