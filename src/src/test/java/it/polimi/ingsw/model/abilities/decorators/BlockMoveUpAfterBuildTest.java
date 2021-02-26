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

class BlockMoveUpAfterBuildTest {

    private Board board;
    private BlockMoveUpAfterBuild abilities;
    private Turn turn;

    @BeforeEach
    void setUp(){
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker1 = new Worker(0, board.getCellFromCoords(0, 0));
        Worker worker2 = new Worker(1, board.getCellFromCoords(1, 1));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);

        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
    }

    /**
     * Check that the decorator doesn't affect a normal build with no move up
     */
    @Test
    void moveNoUp(){
        abilities = new BlockMoveUpAfterBuild(new DefaultAbilities());
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(1,0)));
        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(0, 1)));
        turn.getWorker().move(board.getCellFromCoords(0,1));
        turn.addMovement(board.getCellFromCoords(0,1));
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(1, 1)));
        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(0, 2)));
    }

    /**
     * Check that the decorator doesn't affect a normal build with a move up
     */
    @Test
    void moveUp(){
        abilities = new BlockMoveUpAfterBuild(new DefaultAbilities());
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(1,0)));
        board.getCellFromCoords(0, 1).setLevel(1);
        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(0, 1)));
        turn.getWorker().move(board.getCellFromCoords(0,1));
        turn.addMovement(board.getCellFromCoords(0,1));
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(1, 1)));
        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(0, 2)));
    }

    /**
     * Check that the decorator doesn't affect a decorated build with no move up
     */
    @Test
    void noMoveUpWithAbility(){
        abilities = new BlockMoveUpAfterBuild(new BuildBeforeMove(new DefaultAbilities()));
        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(1,0)));
        abilities.doBuildBlock(turn,board.getCellFromCoords(1,0));
        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(0, 1)));
        turn.getWorker().move(board.getCellFromCoords(0,1));
        turn.addMovement(board.getCellFromCoords(0,1));
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(1, 1)));
        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(0, 2)));

    }

    /**
     * Check that the decorator affects a decorated build with a move up
     */
    @Test
    void moveUpWithAbility(){
        abilities = new BlockMoveUpAfterBuild(new BuildBeforeMove(new DefaultAbilities()));
        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(1,0)));
        abilities.doBuildBlock(turn,board.getCellFromCoords(1,0));
        board.getCellFromCoords(0, 1).setLevel(1);
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(0, 1)));
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(1, 1)));
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(0, 2)));
    }

}

