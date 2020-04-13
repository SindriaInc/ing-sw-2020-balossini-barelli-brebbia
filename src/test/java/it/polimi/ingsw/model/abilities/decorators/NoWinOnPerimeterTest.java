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

class NoWinOnPerimeterTest {

    private Board board;
    private NoWinOnPerimeter noWinOnPerimeter;
    private Turn turn;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker = new Worker(board.getCellFromCoords(0, 0));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();

        noWinOnPerimeter = new NoWinOnPerimeter(new DefaultAbilities(), List.of());
        turn = new Turn(worker, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));

        board.getCellFromCoords(0, 0).setLevel(2);
        board.getCellFromCoords(2, 0).setLevel(3);
        board.getCellFromCoords(1, 1).setLevel(3);
    }

    /**
     * Check that a worker can win by moving in an inner cell
     */
    @Test
    void checkCanWin() {
        assertFalse(noWinOnPerimeter.checkHasWon(turn));

        noWinOnPerimeter.doMove(turn, board.getCellFromCoords(1, 1));

        assertTrue(noWinOnPerimeter.checkHasWon(turn));
    }

    /**
     * Check that a worker can't win by moving in an outer cell
     */
    @Test
    void checkCannotWin() {
        noWinOnPerimeter.doMove(turn, board.getCellFromCoords(0, 1));

        assertFalse(noWinOnPerimeter.checkHasWon(turn));
    }

}