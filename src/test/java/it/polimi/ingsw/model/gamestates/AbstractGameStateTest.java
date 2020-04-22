package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractGameStateTest {

    private Board board;
    private List<Player> players;
    private AbstractGameState abstractGameState;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        players = List.of(new Player("A", 0), new Player("B", 1));
        abstractGameState = new AbstractGameState(board, players) {

            @Override
            public AbstractGameState nextState() {
                return null;
            }

            @Override
            public Player getCurrentPlayer() {
                return null;
            }

            @Override
            public boolean isEnded() {
                return false;
            }

        };
    }

    @Test
    void testDefaults() {
        assertThrows(IllegalStateException.class, () -> abstractGameState.getAvailableGods());
        assertThrows(IllegalStateException.class, () -> abstractGameState.getSelectGodsCount());
        assertThrows(IllegalStateException.class, () -> abstractGameState.checkCanSelectGods(List.of()));
        assertThrows(IllegalStateException.class, () -> abstractGameState.selectGods(List.of()));
        assertThrows(IllegalStateException.class, () -> abstractGameState.chooseGod(null));
        assertThrows(IllegalStateException.class, () -> abstractGameState.getAvailableCells());
        assertThrows(IllegalStateException.class, () -> abstractGameState.spawnWorker(null));
        assertThrows(IllegalStateException.class, () -> abstractGameState.getAvailableMoves(null));
        assertThrows(IllegalStateException.class, () -> abstractGameState.moveWorker(null, null));
        assertThrows(IllegalStateException.class, () -> abstractGameState.getAvailableBlockBuilds(null));
        assertThrows(IllegalStateException.class, () -> abstractGameState.buildBlock(null, null));
        assertThrows(IllegalStateException.class, () -> abstractGameState.getAvailableDomeBuilds(null));
        assertThrows(IllegalStateException.class, () -> abstractGameState.buildDome(null, null));
        assertThrows(IllegalStateException.class, () -> abstractGameState.getAvailableForces(null, null));
        assertThrows(IllegalStateException.class, () -> abstractGameState.forceWorker(null, null, null));
        assertThrows(IllegalStateException.class, () -> abstractGameState.checkCanEndTurn());
        assertThrows(IllegalStateException.class, () -> abstractGameState.endTurn());
    }

    @Test
    void testGetters() {
        assertEquals(board, abstractGameState.getBoard());
        assertEquals(players, abstractGameState.getPlayers());
    }

    @Test
    void checkGetOpponents() {
        List<Player> opponents = new ArrayList<>(abstractGameState.getPlayers());
        Player player = opponents.get(0);
        opponents.remove(player);

        assertEquals(abstractGameState.getOpponents(player), opponents);
    }

    @Test
    void testRemove() {
        List<Player> current = new LinkedList<>(players);
        current.remove(1);

        abstractGameState.removePlayer(players.get(1));
        assertEquals(current, abstractGameState.getPlayers());
    }

    @Test
    void testSort() {
        List<Player> sorted = new LinkedList<>(players);
        Collections.reverse(sorted);

        abstractGameState.sortPlayers(sorted);
        assertEquals(sorted, abstractGameState.getPlayers());
        assertNotEquals(players, abstractGameState.getPlayers());

        sorted.remove(0);
        assertThrows(IllegalArgumentException.class, () -> abstractGameState.sortPlayers(sorted));
    }

}