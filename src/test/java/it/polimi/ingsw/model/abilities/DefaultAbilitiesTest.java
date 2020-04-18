package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultAbilitiesTest {

    private Board board;
    private DefaultAbilities abilities;
    private Turn turn;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        Worker worker1 = new Worker(board.getCellFromCoords(0, 0));
        Worker worker2 = new Worker(board.getCellFromCoords(1, 0));

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);

        abilities = new DefaultAbilities();
        turn = new Turn(worker1, otherWorkers, (cell) -> board.getNeighborings(cell), cell -> false);
    }

    @Test
    void checkWinCondition() {
        getCell(0, 0).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL - 1);
        getCell(0, 1).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL);

        assertFalse(abilities.checkHasWon(turn));

        abilities.doMove(turn, getCell(0, 1));
        assertTrue(abilities.checkHasWon(turn));
    }

    @Test
    void checkNoUpNoWin() {
        getCell(0, 0).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL);
        getCell(0, 1).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL);

        assertFalse(abilities.checkHasWon(turn));

        abilities.doMove(turn, getCell(0, 1));
        assertFalse(abilities.checkHasWon(turn));
    }

    @Test
    void checkNoWinLevelNoWin() {
        getCell(0, 0).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL - 2);
        getCell(0, 1).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL - 1);

        assertFalse(abilities.checkHasWon(turn));

        abilities.doMove(turn, getCell(0, 1));
        assertFalse(abilities.checkHasWon(turn));
    }

    @Test
    void checkNoMovementNoWin() {
        getCell(0, 0).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL);

        assertFalse(abilities.checkHasWon(turn));
    }

    @Test
    void checkCanMove() {
        getCell(0, 1).setLevel(1);
        getCell(1, 1).setLevel(2);

        assertFalse(abilities.checkCanMove(turn, getCell(0, 0))); // Moving over the same worker
        assertTrue(abilities.checkCanMove(turn, getCell(0, 1)));
        assertFalse(abilities.checkCanMove(turn, getCell(1, 0))); // Moving over another worker
        assertFalse(abilities.checkCanMove(turn, getCell(1, 1)));

        turn.getWorker().move(getCell(0, 1));

        assertTrue(abilities.checkCanMove(turn, getCell(0, 0)));
        assertFalse(abilities.checkCanMove(turn, getCell(0, 1))); // Moving over the same worker
        assertFalse(abilities.checkCanMove(turn, getCell(1, 0))); // Moving over another worker
        assertTrue(abilities.checkCanMove(turn, getCell(1, 1)));
        assertTrue(abilities.checkCanMove(turn, getCell(0, 2)));
        assertTrue(abilities.checkCanMove(turn, getCell(1, 2)));

        turn.getWorker().move(getCell(1, 1));

        assertFalse(abilities.checkCanMove(turn, getCell(1, 1))); // Moving over the same worker
        assertTrue(abilities.checkCanMove(turn, getCell(0, 0)));
        assertTrue(abilities.checkCanMove(turn, getCell(0, 1)));
        assertTrue(abilities.checkCanMove(turn, getCell(0, 2)));
        assertTrue(abilities.checkCanMove(turn, getCell(1, 2)));
        assertTrue(abilities.checkCanMove(turn, getCell(2, 2)));
        assertTrue(abilities.checkCanMove(turn, getCell(2, 1)));
        assertTrue(abilities.checkCanMove(turn, getCell(2, 0)));
        assertFalse(abilities.checkCanMove(turn, getCell(1, 0))); // Moving over another worker

        // The worker can't move more than one time
        abilities.doMove(turn, getCell(1, 1));

        assertFalse(abilities.checkCanMove(turn, getCell(1, 1))); // Moving over the same worker
        assertFalse(abilities.checkCanMove(turn, getCell(0, 0)));
        assertFalse(abilities.checkCanMove(turn, getCell(0, 1)));
        assertFalse(abilities.checkCanMove(turn, getCell(0, 2)));
        assertFalse(abilities.checkCanMove(turn, getCell(1, 2)));
        assertFalse(abilities.checkCanMove(turn, getCell(2, 2)));
        assertFalse(abilities.checkCanMove(turn, getCell(2, 1)));
        assertFalse(abilities.checkCanMove(turn, getCell(2, 0)));
        assertFalse(abilities.checkCanMove(turn, getCell(1, 0))); // Moving over another worker
    }

    @Test
    void doMove() {
        abilities.doMove(turn, getCell(0, 1));
        assertEquals(turn.getWorker().getCell(), getCell(0, 1));
    }

    @Test
    void checkCanBuildBlock() {
        getCell(0, 1).setLevel(1);
        getCell(1, 1).setLevel(2);

        assertFalse(abilities.checkCanBuildBlock(turn, getCell(0, 0))); // Building over the same worker
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(0, 1)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(1, 0))); // Building over another worker
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(1, 1)));

        // The worker needs to move before being able to build
        abilities.doMove(turn, getCell(0, 0));

        assertFalse(abilities.checkCanBuildBlock(turn, getCell(0, 0))); // Building over the same worker
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(0, 1)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(1, 0))); // Building over another worker
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(1, 1)));

        turn.getWorker().move(getCell(0, 1));

        assertTrue(abilities.checkCanBuildBlock(turn, getCell(0, 0)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(1, 0))); // Building over another worker
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(1, 1)));
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(0, 2)));
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(1, 2)));

        turn.getWorker().move(getCell(1, 1));

        assertFalse(abilities.checkCanBuildBlock(turn, getCell(1, 1))); // Building over the same worker
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(0, 0)));
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(0, 1)));
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(0, 2)));
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(1, 2)));
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(2, 2)));
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(2, 1)));
        assertTrue(abilities.checkCanBuildBlock(turn, getCell(2, 0)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(1, 0))); // Building over another worker

        // The worker can't build more than one time
        abilities.doBuildBlock(turn, getCell(1, 1));

        assertFalse(abilities.checkCanBuildBlock(turn, getCell(1, 1))); // Building over the same worker
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(0, 0)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(0, 1)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(0, 2)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(1, 2)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(2, 2)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(2, 1)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(2, 0)));
        assertFalse(abilities.checkCanBuildBlock(turn, getCell(1, 0))); // Building over another worker
    }

    @Test
    void doBuildBlock() {
        abilities.doBuildBlock(turn, getCell(0, 1));

        assertEquals(getCell(0, 1).getLevel(), 1);
    }

    @Test
    void checkCanBuildDome() {
        getCell(0, 1).setLevel(1);
        getCell(1, 1).setLevel(2);
        getCell(2, 1).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);

        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 0))); // Building over the same worker
        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 1)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 0))); // Building over another worker
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 1)));

        // The worker needs to move before being able to build
        abilities.doMove(turn, getCell(0, 0));

        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 0))); // Building over the same worker
        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 1)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 0))); // Building over another worker
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 1)));

        turn.getWorker().move(getCell(0, 1));

        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 0)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 0))); // Building over another worker
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 1)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 2)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 2)));

        turn.getWorker().move(getCell(1, 1));

        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 1))); // Building over the same worker
        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 0)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 1)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 2)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 2)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(2, 2)));
        assertTrue(abilities.checkCanBuildDome(turn, getCell(2, 1)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(2, 0)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 0))); // Building over another worker

        // The worker can't build more than one time
        abilities.doBuildDome(turn, getCell(1, 1));

        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 1))); // Building over the same worker
        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 0)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 1)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(0, 2)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 2)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(2, 2)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(2, 1)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(2, 0)));
        assertFalse(abilities.checkCanBuildDome(turn, getCell(1, 0))); // Building over another worker
    }

    @Test
    void doBuildDome() {
        getCell(0, 1).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);

        abilities.doBuildDome(turn, getCell(0, 1));

        assertTrue(getCell(0, 1).isDoomed());
    }

    private Cell getCell(int x, int y) {
        return board.getCellFromCoords(x, y);
    }

}