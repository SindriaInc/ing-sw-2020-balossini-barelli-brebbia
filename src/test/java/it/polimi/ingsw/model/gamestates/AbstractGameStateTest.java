package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
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
        assertNull(abstractGameState.getAvailableGods());
        assertNull(abstractGameState.getSelectGodsCount());
        assertFalse(abstractGameState.checkCanSelectGods(List.of()));
        assertEquals(Game.ModelResponse.INVALID_STATE, abstractGameState.selectGods(List.of()));
        assertEquals(Game.ModelResponse.INVALID_STATE, abstractGameState.chooseGod(null));
        assertNull(abstractGameState.getAvailableCells());
        assertEquals(Game.ModelResponse.INVALID_STATE, abstractGameState.spawnWorker(null));
        assertNull(abstractGameState.getAvailableMoves(null));
        assertEquals(Game.ModelResponse.INVALID_STATE, abstractGameState.moveWorker(null, null));
        assertNull(abstractGameState.getAvailableBlockBuilds(null));
        assertEquals(Game.ModelResponse.INVALID_STATE, abstractGameState.buildBlock(null, null));
        assertNull(abstractGameState.getAvailableDomeBuilds(null));
        assertEquals(Game.ModelResponse.INVALID_STATE, abstractGameState.buildDome(null, null));
        assertNull(abstractGameState.getAvailableForces(null, null));
        assertEquals(Game.ModelResponse.INVALID_STATE, abstractGameState.forceWorker(null, null, null));
        assertFalse(abstractGameState.checkCanEndTurn());
        assertEquals(Game.ModelResponse.INVALID_STATE, abstractGameState.endTurn());
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