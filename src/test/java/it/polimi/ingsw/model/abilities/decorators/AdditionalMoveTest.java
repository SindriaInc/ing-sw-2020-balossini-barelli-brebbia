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

class AdditionalMoveTest {

    private Board board;
    private AdditionalMove abilities;
    private Turn turn;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker1 = new Worker(board.getCellFromCoords(0, 0));
        Worker worker2 = new Worker(board.getCellFromCoords(1, 1));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);

        abilities = new AdditionalMove(new DefaultAbilities());
        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> board.isPerimeterSpace(cell));
        turn.getWorker().move(board.getCellFromCoords(1,0));
        turn.addMovement(board.getCellFromCoords(1,0));
    }

    /**
     * Check that a worker with this power can move twice
     */
    @Test
    void checkCanMoveInAnotherFreeCell() {
        assertTrue(abilities.checkCanMove(turn, board.getCellFromCoords(2,0)));
    }

    /**
     * Check that a worker with this power can't move another time on occupied cell
     */
    @Test
    void checkCannotMoveInOccupiedCell(){
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(1,1)));
    }

    /**
     * Check that a worker with this power can't return on the initial cell
     */
    @Test
    void checkCannotMoveBack(){
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(0,0)));
    }

    /**
     * Check that a worker with this power can't move three times
     */
    @Test
    void checkNoMoveThreeTimes(){
        abilities.doMove(turn,board.getCellFromCoords(2,0));
        assertFalse(abilities.checkCanMove(turn, board.getCellFromCoords(3,0)));
    }

}

