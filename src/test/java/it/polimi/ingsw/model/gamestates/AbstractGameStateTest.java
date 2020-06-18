package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractGameStateTest {

    private Board board;
    private List<Player> players;
    private AbstractGameState abstractGameState;
    private ModelEventProvider modelEventProvider;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        players = List.of(new Player("A", 0), new Player("B", 1));
        modelEventProvider = new ModelEventProvider();
        abstractGameState = new AbstractGameState(modelEventProvider, board, players) {

            @Override
            public AbstractGameState nextState() {
                return null;
            }

            @Override
            public Player getCurrentPlayer() {
                return null;
            }

        };
    }

    @Test
    void testDefaults() {
        assertEquals(ModelResponse.INVALID_STATE, abstractGameState.selectGods(List.of()));
        assertEquals(ModelResponse.INVALID_STATE, abstractGameState.chooseGod(null));
        assertEquals(ModelResponse.INVALID_STATE, abstractGameState.selectFirst(null));
        assertEquals(ModelResponse.INVALID_STATE, abstractGameState.spawnWorker(null));
        assertEquals(ModelResponse.INVALID_STATE, abstractGameState.moveWorker(0, null));
        assertEquals(ModelResponse.INVALID_STATE, abstractGameState.buildBlock(0, null));
        assertEquals(ModelResponse.INVALID_STATE, abstractGameState.buildDome(0, null));
        assertEquals(ModelResponse.INVALID_STATE, abstractGameState.forceWorker(0, 0, null));
        assertEquals(ModelResponse.INVALID_STATE, abstractGameState.endTurn());
    }

    @Test
    void testGetters() {
        assertEquals(board, abstractGameState.getBoard());
        assertEquals(players, abstractGameState.getPlayers());
        assertEquals(modelEventProvider, abstractGameState.getModelEventProvider());
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
        List<Player> current = new ArrayList<>(players);
        current.remove(1);

        abstractGameState.removePlayer(players.get(1));
        assertEquals(current, abstractGameState.getPlayers());
    }

    @Test
    void testSort() {
        List<Player> sorted = new ArrayList<>(players);
        Collections.reverse(sorted);

        abstractGameState.sortPlayers(sorted);
        assertEquals(sorted, abstractGameState.getPlayers());
        assertNotEquals(players, abstractGameState.getPlayers());

        sorted.remove(0);
        assertThrows(IllegalArgumentException.class, () -> abstractGameState.sortPlayers(sorted));
    }

}