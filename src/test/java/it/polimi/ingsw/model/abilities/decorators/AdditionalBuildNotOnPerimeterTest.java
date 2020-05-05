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

class AdditionalBuildNotOnPerimeterTest {

    private Board board;
    private AdditionalBuildNotOnPerimeter abilities;
    private Turn turn;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker1 = new Worker(0, board.getCellFromCoords(2, 2));
        Worker worker2 = new Worker(1, board.getCellFromCoords(1, 1));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker1, false);

        abilities = new AdditionalBuildNotOnPerimeter(new DefaultAbilities());
        turn = new Turn(worker2, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
        turn.addMovement(board.getCellFromCoords(1, 1));
        abilities.doBuildBlock(turn, board.getCellFromCoords(2, 0));
    }

    /**
     * Check that a worker with this power can build twice
     */
    @Test
    void checkCanBuildInAnotherCell() {
        assertTrue(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(2,1)));
        assertFalse(abilities.checkCanBuildDome(turn, board.getCellFromCoords(2,1)));
        board.getCellFromCoords(2, 1).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertTrue(abilities.checkCanBuildDome(turn, board.getCellFromCoords(2,1)));
    }

    /**
     * Check that a worker with this power can't build on occupied cell
     */
    @Test
    void checkCannotBuildInOccupiedCell() {
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(2,2)));
        assertFalse(abilities.checkCanBuildDome(turn, board.getCellFromCoords(2,2)));
        board.getCellFromCoords(2, 2).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertFalse(abilities.checkCanBuildDome(turn, board.getCellFromCoords(2,2)));
    }

    /**
     * Check that a worker with this power can't build the second time on a perimetrical cell
     */
    @Test
    void checkCannotBuildSameCell() {
        assertFalse(abilities.checkCanBuildBlock(turn, board.getCellFromCoords(2,0)));
        assertFalse(abilities.checkCanBuildDome(turn, board.getCellFromCoords(2,0)));
        board.getCellFromCoords(2, 2).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertFalse(abilities.checkCanBuildDome(turn, board.getCellFromCoords(2,0)));
    }

    /**
     * Check that a worker with this power can't build three times
     */
    @Test
    void checkNoBuildThreeTimes(){
        abilities.doBuildBlock(turn, board.getCellFromCoords(2,1));
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(2,1)));
    }

}