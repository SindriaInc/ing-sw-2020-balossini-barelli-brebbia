package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.abilities.DefaultAbilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OngoingGameTest {

    private OngoingGame ongoingGame;
    private Board board;
    private List<Player> players;
    private ModelEventProvider modelEventProvider;

    private int requestEndTurnCount;
    private int requestBuildBlockCount;
    private int requestBuildDomeCount;
    private int requestForceCount;
    private int requestMoveCount;
    private int loseCount;
    private int turnStartCount;
    private int buildBlockCount;
    private int buildDomeCount;
    private int forceCount;
    private int moveCount;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        players = new ArrayList<>(List.of(new Player("A", 0), new Player("B", 1)));
        players.get(0).addWorker(new Worker(0, board.getCellFromCoords(0, 0)));
        players.get(0).addWorker(new Worker(1, board.getCellFromCoords(0, 1)));
        players.get(1).addWorker(new Worker(2, board.getCellFromCoords(1, 0)));
        players.get(1).addWorker(new Worker(3, board.getCellFromCoords(1, 1)));
        modelEventProvider = new ModelEventProvider();
    }

    /**
     * Check that a player can't move the worker in a cell not contained in the list of available cells,
     * can't move more than one time (with default abilities) and can't move two workers
     */
    @Test
    void checkMoveSingleWorker() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);

        modelEventProvider.registerRequestWorkerMoveEventObserver(event -> {
            if (requestMoveCount == 0) {
                assertEquals(event.getWorker(), 1);
                assertEquals(event.getAvailableDestinations().size(), 2);
            }
            requestMoveCount++;
        });

        ongoingGame = new OngoingGame(modelEventProvider, board, players);
        assertEquals(requestMoveCount, 1);
        assertEquals(turnStartCount, 1);

        modelEventProvider.registerWorkerMoveEventObserver(event -> {
            if (moveCount == 0) {
                assertEquals(event.getId(), 1);
                assertEquals(event.getDestination(), new Coordinates(0, 2));
            }
            moveCount++;
        });

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.moveWorker(-1, new Coordinates(0, 2)));
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.moveWorker(0, new Coordinates(0, 1)));
        assertEquals(moveCount, 0);
        assertEquals(ModelResponse.ALLOW, ongoingGame.moveWorker(1, new Coordinates(0, 2)));
        assertEquals(requestMoveCount, 1);
        assertEquals(moveCount, 1);

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.moveWorker(0, new Coordinates(0, 1)));
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.moveWorker(1, new Coordinates(0, 3)));
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.moveWorker(1, new Coordinates(0, 1)));

        assertEquals(ongoingGame, ongoingGame.nextState());
        assertEquals(requestMoveCount, 1);
        assertEquals(moveCount, 1);
        assertEquals(turnStartCount, 1);
    }


    /**
     * Check that a player can't build with another worker, can't build on a cell not contained in the list of available cells,
     * can't build more than one time (with default abilities), can't build a dome after building (with default abilities) and
     * can't move after building (with default abilities)
     */
    @Test
    void checkBuildBlock() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);

        modelEventProvider.registerRequestWorkerBuildBlockEventObserver(event -> {
            if (requestBuildBlockCount == 0) {
                assertEquals(event.getWorker(), 1);
                assertEquals(event.getAvailableDestinations().size(), 4);
            }
            requestBuildBlockCount++;
        });

        ongoingGame = new OngoingGame(modelEventProvider, board, players);
        assertEquals(requestBuildBlockCount, 0);
        assertEquals(turnStartCount, 1);

        modelEventProvider.registerWorkerBuildBlockEventObserver(event -> {
            if (buildBlockCount == 0) {
                assertEquals(event.getId(), 1);
                assertEquals(event.getDestination(), new Coordinates(0, 3));
            }
            buildBlockCount++;
        });

        ongoingGame.moveWorker(1, new Coordinates(0, 2));

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildBlock(1, new Coordinates(0, 2)));

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildBlock(0, new Coordinates(0, 2)));

        assertEquals(buildBlockCount, 0);
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildBlock(-1, new Coordinates(0, 3)));
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildBlock(0, new Coordinates(0, 3)));
        ongoingGame.buildBlock(1, new Coordinates(0, 3));
        assertEquals(buildBlockCount, 1);

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildBlock(0, new Coordinates(0, 1)));

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildBlock(1, new Coordinates(1, 3)));

        board.getCellFromCoords(1, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildDome(1, new Coordinates(1, 3)));

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.moveWorker(1, new Coordinates(1, 3)));

        assertEquals(ongoingGame, ongoingGame.nextState());
        assertEquals(buildBlockCount, 1);
        assertEquals(turnStartCount, 1);
        assertEquals(requestBuildBlockCount, 1);
    }

    /**
     * Check that a player can't build a dome with another worker, can't build a dome on a cell not contained in the list of available cells,
     * can't build a dome more than one time (with default abilities), can't build a block after building (with default abilities) and
     * can't move after building (with default abilities)
     */
    @Test
    void checkBuildDome() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);

        modelEventProvider.registerRequestWorkerBuildDomeEventObserver(event -> {
            if (requestBuildDomeCount == 0) {
                assertEquals(event.getWorker(), 1);
                assertEquals(event.getAvailableDestinations().size(), 2);
            }
            requestBuildDomeCount++;
        });

        board.getCellFromCoords(0, 1).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL - 1);
        board.getCellFromCoords(0, 2).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        board.getCellFromCoords(0, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        board.getCellFromCoords(1, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);

        ongoingGame = new OngoingGame(modelEventProvider, board, players);
        assertEquals(requestBuildDomeCount, 0);
        assertEquals(turnStartCount, 1);

        modelEventProvider.registerWorkerBuildDomeEventObserver(event -> {
            if (buildDomeCount == 0) {
                assertEquals(event.getId(), 1);
                assertEquals(event.getDestination(), new Coordinates(0, 3));
            }
            buildDomeCount++;
        });

        ongoingGame.moveWorker(1, new Coordinates(0, 2));
        assertEquals(requestBuildDomeCount, 1);

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildDome(1, new Coordinates(0, 2)));

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildDome(0, new Coordinates(0, 2)));

        assertEquals(buildDomeCount, 0);
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildDome(-1, new Coordinates(0, 3)));
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildDome(2, new Coordinates(0, 3)));
        ongoingGame.buildDome(1, new Coordinates(0, 3));
        assertEquals(buildDomeCount, 1);

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildDome(0, new Coordinates(1, 3)));

        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildDome(1, new Coordinates(1, 3)));

        board.getCellFromCoords(1, 3).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.buildBlock(1, new Coordinates(1, 3)));

        assertEquals(ModelResponse.INVALID_PARAMS,  ongoingGame.moveWorker(1, new Coordinates(1, 3)));

        assertEquals(ongoingGame, ongoingGame.nextState());
        assertEquals(turnStartCount,1);
        assertEquals(requestBuildDomeCount, 1);
        assertEquals(buildDomeCount, 1);
    }

    /**
     * Check that a player can't force with default abilities
     */
    @Test
    void checkDoForce() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);

        modelEventProvider.registerRequestWorkerForceEventObserver(event -> requestForceCount++);

        ongoingGame = new OngoingGame(modelEventProvider, board, players);
        assertEquals(requestForceCount, 0);
        assertEquals(turnStartCount, 1);

        modelEventProvider.registerWorkerForceEventObserver(event -> forceCount++);
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.forceWorker(0, 2, new Coordinates(2, 0)));
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.forceWorker(0, -1, new Coordinates(2, 0)));
        assertEquals(ModelResponse.INVALID_PARAMS, ongoingGame.forceWorker(-1, 2, new Coordinates(2, 0)));
        assertEquals(turnStartCount, 1);
        assertEquals(requestForceCount, 0);
        assertEquals(forceCount, 0);
    }

    /**
     * Check that a player can end his turn only after moving and building
     */
    @Test
    void checkCanEndTurnAfterMovingAndBuilding() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);

        modelEventProvider.registerRequestPlayerEndTurnEventObserver(event -> {
            if (event.getCanBeEnded()) {
                requestEndTurnCount++;
            }
        });

        ongoingGame = new OngoingGame(modelEventProvider, board, players);

        assertEquals(turnStartCount, 1);
        assertEquals(requestEndTurnCount, 0);

        ongoingGame.moveWorker(1, new Coordinates(0, 2));

        assertEquals(turnStartCount, 1);
        assertEquals(requestEndTurnCount, 0);

        ongoingGame.buildBlock(1, new Coordinates(0, 3));

        assertEquals(turnStartCount, 1);
        assertEquals(requestEndTurnCount, 1);

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    /**
     * Check that a player can end his turn only after moving and building
     * The board is setup such that the build must be a dome
     */
    @Test
    void checkCanEndTurnAfterMovingAndBuildingDome() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);

        modelEventProvider.registerRequestPlayerEndTurnEventObserver(event -> {
            if (event.getCanBeEnded()) {
                requestEndTurnCount++;
            }
        });

        board.getCellFromCoords(0, 1).setLevel(3);
        board.getCellFromCoords(0, 2).setLevel(3);
        board.getCellFromCoords(0, 3).setLevel(3);
        board.getCellFromCoords(1, 3).setLevel(3);
        board.getCellFromCoords(1, 2).setLevel(3);

        ongoingGame = new OngoingGame(modelEventProvider, board, players);

        assertEquals(turnStartCount, 1);
        assertEquals(requestEndTurnCount, 0);

        ongoingGame.moveWorker(1, new Coordinates(0, 2));

        assertEquals(turnStartCount, 1);
        assertEquals(requestEndTurnCount, 0);

        ongoingGame.buildDome(1, new Coordinates(0, 3));

        assertEquals(turnStartCount, 1);
        assertEquals(requestEndTurnCount, 1);

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    /**
     * Check that a player and the next player can end their turns
     */
    @Test
    void checkEndTurn() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);

        modelEventProvider.registerRequestPlayerEndTurnEventObserver(event -> {
            if (event.getCanBeEnded()) {
                requestEndTurnCount++;
            }
        });

        ongoingGame = new OngoingGame(modelEventProvider, board, players);

        assertEquals(ModelResponse.INVALID_STATE, ongoingGame.endTurn());

        ongoingGame.moveWorker(1, new Coordinates(0, 2));

        assertEquals(ModelResponse.INVALID_STATE, ongoingGame.endTurn());

        ongoingGame.buildBlock(1, new Coordinates(0, 3));

        ongoingGame.endTurn();
        assertEquals(turnStartCount, 2);
        assertEquals(requestEndTurnCount, 1);

        assertEquals(ModelResponse.INVALID_STATE, ongoingGame.endTurn());

        ongoingGame.moveWorker(2, new Coordinates(2, 0));
        assertEquals(turnStartCount, 2);
        assertEquals(requestEndTurnCount, 1);

        assertEquals(ModelResponse.INVALID_STATE, ongoingGame.endTurn());

        ongoingGame.buildBlock(2, new Coordinates(2, 1));
        assertEquals(turnStartCount, 2);
        assertEquals(requestEndTurnCount, 2);

        ongoingGame.endTurn();
        assertEquals(turnStartCount, 3);
        assertEquals(requestEndTurnCount, 2);

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    /**
     * Check that a player can win given the correct condition
     */
    @Test
    void checkWin() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);

        board.getCellFromCoords(0, 1).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL - 1);
        board.getCellFromCoords(0, 2).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL);
        ongoingGame = new OngoingGame(modelEventProvider, board, players);
        assertEquals(turnStartCount, 1);

        ongoingGame.moveWorker(1, new Coordinates(0, 2));
        assertEquals(turnStartCount, 1);

        ongoingGame.buildBlock(1, new Coordinates(0, 1)); // Need to build before winning

        ongoingGame.endTurn();

        assertNotEquals(ongoingGame, ongoingGame.nextState());
        assertEquals(turnStartCount, 1);
    }

    /**
     * Check that, if trapped, a player loses
     */
    @Test
    void checkLose() {
        modelEventProvider.registerPlayerLoseEventObserver(event -> loseCount++);

        board.getCellFromCoords(0, 2).setLevel(2);
        board.getCellFromCoords(1, 2).setLevel(2);

        ongoingGame = new OngoingGame(modelEventProvider, board, players);
        assertEquals(loseCount, 0);

        ongoingGame.endTurn();
        assertEquals(loseCount, 1);

        assertNotEquals(ongoingGame, ongoingGame.nextState());
    }

    /**
     * Check that, without a build, a player loses
     */
    @Test
    void checkLoseNoBuild() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);
        modelEventProvider.registerPlayerLoseEventObserver(event -> loseCount++);

        // Add a third player so that we can test what happens when a player loses but the other doesn't win
        players.add(new Player("C", 10));
        ongoingGame = new OngoingGame(modelEventProvider, board, players);
        ongoingGame.moveWorker(1, new Coordinates(0, 2));
        board.getCellFromCoords(0, 1).setDoomed(true);
        board.getCellFromCoords(1, 2).setDoomed(true);
        board.getCellFromCoords(1, 3).setDoomed(true);
        board.getCellFromCoords(0, 3).setDoomed(true);


        assertEquals(turnStartCount, 1);
        assertEquals(loseCount, 0);
        ongoingGame.endTurn();
        assertEquals(turnStartCount, 1);
        assertEquals(loseCount, 1);

        assertEquals(ongoingGame, ongoingGame.nextState());
    }

    /**
     * Check that a worker has options after moving
     */
    @Test
    void checkHasOptions() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);
        modelEventProvider.registerPlayerLoseEventObserver(event -> loseCount++);
        ongoingGame = new OngoingGame(modelEventProvider, board, players);
        ongoingGame.moveWorker(1, new Coordinates(0, 2));

        assertEquals(turnStartCount, 1);
        assertEquals(loseCount, 0);

        board.getCellFromCoords(0, 1).setDoomed(true);
        board.getCellFromCoords(1, 2).setDoomed(true);
        board.getCellFromCoords(1, 3).setDoomed(true);
        board.getCellFromCoords(0, 3).setLevel(3);

        assertEquals(turnStartCount, 1);
        assertEquals(loseCount, 0);
    }

}