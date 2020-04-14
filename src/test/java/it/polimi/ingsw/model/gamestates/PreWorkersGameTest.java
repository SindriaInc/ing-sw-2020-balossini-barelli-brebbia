package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.TestConstants.equalsNoOrder;
import static org.junit.jupiter.api.Assertions.*;

class PreWorkersGameTest {

    private PreWorkersGame preWorkersGame;
    private Board board;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        players = List.of(new Player("A", 0), new Player("B", 1));

        preWorkersGame = new PreWorkersGame(board, players, TestConstants.MAX_WORKERS);
    }

    @Test
    void checkGetAvailableCells() {
        assertEquals(preWorkersGame.getCurrentPlayer(), players.get(0));
        assertTrue(equalsNoOrder(board.getCells(), preWorkersGame.getAvailableCells()));

        preWorkersGame.spawnWorker(new Worker(board.getCellFromCoords(0, 0)));

        List<Cell> newCells = new ArrayList<>(board.getCells());
        newCells.remove(board.getCellFromCoords(0, 0));

        assertTrue(equalsNoOrder(newCells, preWorkersGame.getAvailableCells()));
    }

    @Test
    void checkSpawnWorker() {
        assertThrows(IllegalArgumentException.class, () -> preWorkersGame.spawnWorker(new Worker(new Cell(-1, -1))));

        preWorkersGame.spawnWorker(new Worker(board.getCellFromCoords(0, 0)));

        assertEquals(preWorkersGame, preWorkersGame.nextState());
        assertEquals(preWorkersGame.getCurrentPlayer(), players.get(0));

        // Can't spawn a worker over an existing worker
        assertThrows(IllegalArgumentException.class, () -> preWorkersGame.spawnWorker(new Worker(board.getCellFromCoords(0, 0))));

        preWorkersGame.spawnWorker(new Worker(board.getCellFromCoords(0, 1)));

        assertEquals(preWorkersGame, preWorkersGame.nextState());
        assertEquals(preWorkersGame.getCurrentPlayer(), players.get(1));

        preWorkersGame.spawnWorker(new Worker(board.getCellFromCoords(1, 0)));
        preWorkersGame.spawnWorker(new Worker(board.getCellFromCoords(1, 1)));

        assertNotEquals(preWorkersGame, preWorkersGame.nextState());
    }

}