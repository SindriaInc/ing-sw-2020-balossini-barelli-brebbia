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

    /**
     * Check the available cell to move the worker
     */
    @Test
    void checkGetAvailableMoves() {
        assertEquals(0, ongoingGame.getAvailableMoves(getWorker(0, 0)).size());
        assertEquals(2, ongoingGame.getAvailableMoves(getWorker(0, 1)).size());
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.getAvailableMoves(getWorker(1, 0)));
    }

    /**
     * Check that a player can't move the worker in a cell not contained in the list of available cells,
     * can't move more than one time (with default abilities) and can't move two workers
     */
    @Test
    void checkMoveSingleWorker() {
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.moveWorker(getWorker(0, 0), board.getCellFromCoords(0, 1)));

        Worker worker = getWorker(0, 1);
        ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 2));

        assertEquals(0, ongoingGame.getAvailableMoves(worker).size());
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 3)));

        Worker worker2 = players.get(0).getWorkers().get(0);
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 1)));

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    /**
     * Check that a player can't build before moving with default abilities
     */
    @Test
    void checkGetAvailableBlockAndDomeBuilds() {
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

    /**
     * Check that a player can't build with another worker, can't build on a cell not contained in the list of available cells,
     * can't build more than one time (with default abilities), can't build a dome after building (with default abilities) and
     * can't move after building (with default abilities)
     */
    @Test
    void checkBuildBlock() {
        Worker worker = getWorker(0, 1);

        ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 2));

        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildBlock(worker, board.getCellFromCoords(0, 2)));

        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildBlock(getWorker(0, 0), board.getCellFromCoords(0, 2)));

        ongoingGame.buildBlock(worker, board.getCellFromCoords(0, 3));

        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildBlock(worker, board.getCellFromCoords(1, 3)));

        board.getCellFromCoords(1, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildDome(worker, board.getCellFromCoords(1, 3)));

        assertThrows(IllegalArgumentException.class, () -> ongoingGame.moveWorker(worker, board.getCellFromCoords(1, 3)));

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    /**
     * Check that a player can't build a dome with another worker, can't build a dome on a cell not contained in the list of available cells,
     * can't build a dome more than one time (with default abilities), can't build a block after building (with default abilities) and
     * can't move after building (with default abilities)
     */
    @Test
    void checkBuildDome() {
        Worker worker = getWorker(0, 1);

        ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 2));

        board.getCellFromCoords(0, 2).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        board.getCellFromCoords(0, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        board.getCellFromCoords(1, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);

        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildDome(worker, board.getCellFromCoords(0, 2)));

        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildDome(getWorker(0, 0), board.getCellFromCoords(0, 2)));

        ongoingGame.buildDome(worker, board.getCellFromCoords(0, 3));

        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildDome(worker, board.getCellFromCoords(1, 3)));

        board.getCellFromCoords(1, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.buildBlock(worker, board.getCellFromCoords(1, 3)));

        assertThrows(IllegalArgumentException.class, () -> ongoingGame.moveWorker(worker, board.getCellFromCoords(1, 3)));

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    /**
     * Check that, with default abilities, can't be offered to a player to force
     */
    @Test
    void checkGetAvailableForces() {
        assertEquals(0, ongoingGame.getAvailableForces(getWorker(0, 0), getWorker(1, 0)).size());
    }

    /**
     * Check that a player can't force with default abilities
     */
    @Test
    void checkDoForce() {
        assertThrows(IllegalArgumentException.class, () -> ongoingGame.forceWorker(getWorker(0, 0), getWorker(1, 0), board.getCellFromCoords(2, 0)));
    }

    /**
     * Check that a player can end his turn only after moving and building
     */
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

    /**
     * Check that a player and the next player can end their turns
     */
    @Test
    void checkEndTurn() {
        Worker worker = getWorker(0, 1);

        assertThrows(IllegalStateException.class, () -> ongoingGame.endTurn());

        ongoingGame.moveWorker(worker, board.getCellFromCoords(0, 2));

        assertThrows(IllegalStateException.class, () -> ongoingGame.endTurn());

        ongoingGame.buildBlock(worker, board.getCellFromCoords(0, 3));

        ongoingGame.endTurn();

        Worker worker2 = getWorker(1, 0);

        assertThrows(IllegalStateException.class, () -> ongoingGame.endTurn());

        ongoingGame.moveWorker(worker2, board.getCellFromCoords(2, 0));

        assertThrows(IllegalStateException.class, () -> ongoingGame.endTurn());

        ongoingGame.buildBlock(worker2, board.getCellFromCoords(2, 1));

        ongoingGame.endTurn();

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    /**
     * Check that, if trapped, a player loses
     */
    @Test
    void checkLose() {
        board.getCellFromCoords(0, 2).setLevel(2);
        board.getCellFromCoords(1, 2).setLevel(2);

        ongoingGame.endTurn();

        assertNotEquals(ongoingGame, ongoingGame.nextState());
    }

    private Worker getWorker(int player, int worker) {
        return players.get(player).getWorkers().get(worker);
    }

}