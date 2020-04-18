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

class BuildBeforeMoveTest {

    private Board board;
    private BuildBeforeMove abilities;
    private Turn turn;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker1 = new Worker(board.getCellFromCoords(3, 3));
        Worker worker2 = new Worker(board.getCellFromCoords(4, 4));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);

        abilities = new BuildBeforeMove(new DefaultAbilities());
        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
    }

    /**
     * Check that the decorator doesn't affect a normal build
     */
    @Test
    void checkNoEffectBuild() {
        abilities.doMove(turn, board.getCellFromCoords(3, 4));

        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(3, 3)));
        assertFalse(abilities.checkCanBuildDome(turn, board.getCellFromCoords(3, 3)));
        board.getCellFromCoords(3, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(3, 3)));
        assertTrue(abilities.checkCanBuildDome(turn, board.getCellFromCoords(3, 3)));

        abilities.doBuildDome(turn, board.getCellFromCoords(3, 3));
        board.getCellFromCoords(3, 3).setDoomed(false);
        assertFalse(abilities.checkCanBuildDome(turn, board.getCellFromCoords(3, 3)));
        board.getCellFromCoords(3, 3).setLevel(0);
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(3, 3)));
    }

    /**
     * Check that a worker with this power can build a block before and after his move
     */
    @Test
    void checkCanMoveAndBuildBlockAfterFirstBuild() {
        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(3,4)));
        abilities.doBuildBlock(turn,board.getCellFromCoords(3, 4));

        board.getCellFromCoords(4, 3).setLevel(2);
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(4, 3))); // Can't move on a too high cell
        board.getCellFromCoords(4, 3).setLevel(0);
        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(4, 3)));
        abilities.doMove(turn, board.getCellFromCoords(4,3));

        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(4, 2)));
    }

    /**
     * Check that a worker with this power can build a dome after his move
     */
    @Test
    void checkCanMoveAndBuildDomeAfterFirstBuild() {
        assertFalse(abilities.checkCanBuildDome(turn, board.getCellFromCoords(3,4)));
        board.getCellFromCoords(3,4).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertTrue(abilities.checkCanBuildDome(turn, board.getCellFromCoords(3,4)));
        abilities.doBuildDome(turn, board.getCellFromCoords(3, 4));

        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(4, 3)));
        abilities.doMove(turn, board.getCellFromCoords(4,3));

        assertFalse(abilities.checkCanBuildDome(turn, board.getCellFromCoords(4, 2)));
        board.getCellFromCoords(4,2).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertTrue(abilities.checkCanBuildDome(turn, board.getCellFromCoords(4, 2)));
    }

    /**
     * Check that a worker with this power can't build before and after his move on another worker
     */
    @Test
    void checkCannotMoveAndBuildAfterFirstBuildOnAWorker() {
        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(3,4)));
        abilities.doBuildBlock(turn,board.getCellFromCoords(3, 4));

        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(4, 3)));
        abilities.doMove(turn, board.getCellFromCoords(4,3));

        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(4, 4)));
    }

    /**
     * Check that a worker with this power can't build two times without moving
     */
    @Test
    void checkCannotBuildAfterFirstBlock() {
        abilities.doBuildBlock(turn, board.getCellFromCoords(3, 4));
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(4,3)));
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(4,3)));
        board.getCellFromCoords(4, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(4,3)));
    }

    /**
     * Check that a worker with this power can't move twice after the second build
     */
    @Test
    void checkCannotMoveTwice() {
        abilities.doBuildBlock(turn,board.getCellFromCoords(3, 4));
        abilities.doMove(turn, board.getCellFromCoords(4,3));
        abilities.doBuildBlock(turn, board.getCellFromCoords(4, 2));
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(4,1)));
    }

}

