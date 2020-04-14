package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TestConstants;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.DefaultAbilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OngoingGameTest {

    private OngoingGame ongoingGame;
    private Board board;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        players = List.of(new Player("A", 0), new Player("B", 1));
        players.get(0).addWorker(new Worker(board.getCellFromCoords(0, 0)));
        players.get(0).addWorker(new Worker(board.getCellFromCoords(0, 1)));
        players.get(1).addWorker(new Worker(board.getCellFromCoords(1, 0)));
        players.get(1).addWorker(new Worker(board.getCellFromCoords(1, 1)));

        ongoingGame = new OngoingGame(board, players);
    }

    @Test
    void checkGetAvailableMoves() {
        assertEquals(0, ongoingGame.getAvailableMoves(getWorker(0, 0)).size());
        assertEquals(2, ongoingGame.getAvailableMoves(getWorker(0, 1)).size());
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.getAvailableMoves(getWorker(1, 0)));
    }

    @Test
    void checkMoveSingleWorker() {
        // Can't move the worker in a cell not contained in the list of available cells
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.moveWorker(getWorker(0, 0), board.getCellFromCoords(0, 1)));

        Worker worker = getWorker(0, 1);
        ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 2));

        // Can't move more than one time (with default abilities)
        assertEquals(0, ongoingGame.getAvailableMoves(worker).size());
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 3)));

        // Can't move two workers
        Worker worker2 = players.get(0).getWorkers().get(0);
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 1)));

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    @Test
    void checkGetAvailableBlockAndDomeBuilds() {
        // Can't build before moving with default abilities
        assertEquals(0, ongoingGame.getAvailableBlockBuilds(getWorker(0, 0)).size());
        assertEquals(0, ongoingGame.getAvailableBlockBuilds(getWorker(0, 1)).size());
        assertEquals(0, ongoingGame.getAvailableDomeBuilds(getWorker(0, 0)).size());
        assertEquals(0, ongoingGame.getAvailableDomeBuilds(getWorker(0, 1)).size());

        Worker worker = getWorker(0, 1);

        ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 2));

        assertEquals(4, ongoingGame.getAvailableBlockBuilds(worker).size());
        assertEquals(0, ongoingGame.getAvailableDomeBuilds(worker).size());

        board.getCellFromCoords(1, 2).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);

        assertEquals(3, ongoingGame.getAvailableBlockBuilds(worker).size());
        assertEquals(1, ongoingGame.getAvailableDomeBuilds(worker).size());
    }

    @Test
    void checkBuildBlock() {
        Worker worker = getWorker(0, 1);

        ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 2));

        // Can't build with another worker
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildBlock(worker, board.getCellFromCoords(0, 2)));

        // Can't build on a cell not contained in the list of available cells
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildBlock(getWorker(0, 0), board.getCellFromCoords(0, 2)));

        ongoingGame.buildBlock(worker, board.getCellFromCoords(0, 3));

        // Can't build more than one time (with default abilities)
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildBlock(worker, board.getCellFromCoords(1, 3)));

        // Can't build a dome after building (with default abilities)
        board.getCellFromCoords(1, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildDome(worker, board.getCellFromCoords(1, 3)));

        // Can't move after building (with default abilities)
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.moveWorker(worker, board.getCellFromCoords(1, 3)));

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    @Test
    void checkBuildDome() {
        Worker worker = getWorker(0, 1);

        ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 2));

        board.getCellFromCoords(0, 2).setLevel(3);
        board.getCellFromCoords(0, 3).setLevel(3);
        board.getCellFromCoords(1, 3).setLevel(3);

        // Can't build with another worker
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildDome(worker, board.getCellFromCoords(0, 2)));

        // Can't build on a cell not contained in the list of available cells
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildDome(getWorker(0, 0), board.getCellFromCoords(0, 2)));

        ongoingGame.buildDome(worker, board.getCellFromCoords(0, 3));

        // Can't build more than one time (with default abilities)
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildDome(worker, board.getCellFromCoords(1, 3)));

        // Can't build a block after building (with default abilities)
        board.getCellFromCoords(1, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildBlock(worker, board.getCellFromCoords(1, 3)));

        // Can't move after building (with default abilities)
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.moveWorker(worker, board.getCellFromCoords(1, 3)));

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    @Test
    void checkGetAvailableForces() {
        // Can't force with default abilities
        assertEquals(0, ongoingGame.getAvailableForces(getWorker(0, 0), getWorker(1, 0)).size());
    }

    @Test
    void checkDoForce() {
        // Can't force with default abilities
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.forceWorker(getWorker(0, 0), getWorker(1, 0), board.getCellFromCoords(2, 0)));
    }

    @Test
    void checkCanEndTurnAfterMovingAndBuilding() {
        Worker worker = getWorker(0, 1);

        assertFalse(ongoingGame.checkCanEndTurn());

        ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 2));

        assertFalse(ongoingGame.checkCanEndTurn());

        ongoingGame.buildBlock(worker, board.getCellFromCoords(0, 3));

        assertTrue(ongoingGame.checkCanEndTurn());

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    @Test
    void checkEndTurn() {
        Worker worker = getWorker(0, 1);

        assertThrows(IllegalStateException.class, () -> ongoingGame.endTurn());

        ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 2));

        assertThrows(IllegalStateException.class, () -> ongoingGame.endTurn());

        ongoingGame.buildBlock(worker, board.getCellFromCoords(0, 3));

        ongoingGame.endTurn();

        // Check for the next player
        Worker worker2 = getWorker(1, 0);

        assertThrows(IllegalStateException.class, () -> ongoingGame.endTurn());

        ongoingGame.moveWorker(worker2, board.getCellFromCoords(2, 0));

        assertThrows(IllegalStateException.class, () -> ongoingGame.endTurn());

        ongoingGame.buildBlock(worker2, board.getCellFromCoords(2, 1));

        ongoingGame.endTurn();

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    @Test
    void checkLose() {
        // Trap the workers in their current position
        board.getCellFromCoords(0, 2).setLevel(2);
        board.getCellFromCoords(1, 2).setLevel(2);

        ongoingGame.endTurn();

        assertNotEquals(ongoingGame, ongoingGame.nextState());
    }

    private Worker getWorker(int player, int worker) {
        return players.get(player).getWorkers().get(worker);
    }

}