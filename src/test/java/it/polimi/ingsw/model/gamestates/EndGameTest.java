package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndGameTest {

    private EndGame endGame;
    private Board board;
    private Player winner;
    private ModelEventProvider modelEventProvider;

    private boolean checkEventCalled = false;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        winner = new Player("A", 0);
        winner.addWorker(new Worker(0, board.getCellFromCoords(0, 0)));
        winner.addWorker(new Worker(1, board.getCellFromCoords(0, 1)));
        modelEventProvider = new ModelEventProvider();
    }

    /**
     * Check that the current player is the winner
     */
    @Test
    void checkGetCurrentPlayer() {
        endGame = new EndGame(modelEventProvider, board, winner);
        assertEquals(winner, endGame.getCurrentPlayer());
    }

    /**
     * Check that there is no next state
     */
    @Test
    void checkNextState() {
        endGame = new EndGame(modelEventProvider, board, winner);
        assertEquals(endGame, endGame.nextState());
    }

    @Test
    void checkEvent() {
        modelEventProvider.registerPlayerWinEventObserver(event -> {
            checkEventCalled = true;
            assertEquals(event.getPlayer(), winner.getName());
        });
        endGame = new EndGame(modelEventProvider, board, winner);
        assertTrue(checkEventCalled);
    }
}