package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.Coordinates;
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
    ModelEventProvider modelEventProvider;

    private int turnStartCount;
    private int requestSpawnCount;
    private int spawnCount;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        players = List.of(new Player("A", 0), new Player("B", 1));
        modelEventProvider = new ModelEventProvider();
    }

    /**
     * Check that a player can spawn a worker but not on an existing worker
     */
    @Test
    void checkSpawnWorker() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);

        modelEventProvider.registerRequestWorkerSpawnEventObserver(event -> {
            assertEquals(event.getAvailablePositions().size(), TestConstants.BOARD_TEST_COLUMNS * TestConstants.BOARD_TEST_ROWS - requestSpawnCount);
            requestSpawnCount++;
        });

        preWorkersGame = new PreWorkersGame(modelEventProvider, board, players, TestConstants.MAX_WORKERS);
        assertEquals(requestSpawnCount, 1);
        assertEquals(turnStartCount, 1);

        modelEventProvider.registerWorkerSpawnEventObserver(event -> {
            if (spawnCount == 0) {
                assertEquals(event.getPosition(), new Coordinates(0, 0));
            } else if (spawnCount == 1) {
                assertEquals(event.getPosition(), new Coordinates(0, 1));
            } else if (spawnCount == 2) {
                assertEquals(event.getPosition(), new Coordinates(1, 0));
            } else if (spawnCount == 3) {
                assertEquals(event.getPosition(), new Coordinates(1, 1));
            }
            spawnCount++;
        });

        assertEquals(Game.ModelResponse.INVALID_PARAMS, preWorkersGame.spawnWorker(new Coordinates(-1,-1)));
        assertEquals(spawnCount, 0);
        preWorkersGame.spawnWorker(new Coordinates(0, 0));
        assertEquals(requestSpawnCount, 2);
        assertEquals(spawnCount, 1);
        assertEquals(turnStartCount, 1);

        assertEquals(preWorkersGame, preWorkersGame.nextState());
        assertEquals(preWorkersGame.getCurrentPlayer(), players.get(0));

        assertEquals(Game.ModelResponse.INVALID_PARAMS, preWorkersGame.spawnWorker(new Coordinates(0, 0)));

        preWorkersGame.spawnWorker(new Coordinates(0, 1));
        assertEquals(requestSpawnCount, 3);
        assertEquals(spawnCount, 2);
        assertEquals(turnStartCount, 2);

        assertEquals(preWorkersGame, preWorkersGame.nextState());
        assertEquals(preWorkersGame.getCurrentPlayer(), players.get(1));

        preWorkersGame.spawnWorker(new Coordinates(1, 0));
        assertEquals(requestSpawnCount, 4);
        assertEquals(spawnCount, 3);
        assertEquals(turnStartCount, 2);
        preWorkersGame.spawnWorker(new Coordinates(1, 1));

        // Goes into the next state
        assertEquals(requestSpawnCount, 4);
        assertEquals(spawnCount, 4);
        assertNotEquals(preWorkersGame, preWorkersGame.nextState());
    }

}