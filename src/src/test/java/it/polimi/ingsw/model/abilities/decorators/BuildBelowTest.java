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

import static it.polimi.ingsw.model.abilities.DefaultAbilities.DEFAULT_MAX_BUILD_LEVEL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildBelowTest {

    private Board board;
    private BuildBelow abilities;
    private Turn turn;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker = new Worker(0, board.getCellFromCoords(0, 0));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();

        abilities = new BuildBelow(new DefaultAbilities());
        turn = new Turn(worker, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
    }

    /**
     * Check that a worker with this ability can build below itself
     * Check that he can't build if his level is greater equal than than max build level
     * Check that he can't build a second time
     */
    @Test
    void checkBuildBelow() {

        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(0,0)));

        turn.addMovement(board.getCellFromCoords(1, 1));

        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(0,0)));

        board.getCellFromCoords(0, 0).setLevel(DEFAULT_MAX_BUILD_LEVEL);
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(0,0)));

        board.getCellFromCoords(0, 0).setLevel(DEFAULT_MAX_BUILD_LEVEL-1);
        turn.addBlockPlaced(board.getCellFromCoords(0, 0));
        assertFalse(abilities.checkHasWon(turn));

        board.getCellFromCoords(0, 0).setLevel(0);
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(0,0)));
    }
}