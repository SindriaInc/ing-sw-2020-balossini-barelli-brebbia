package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TestConstants;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndGameTest {

    private EndGame endGame;
    private Player winner;

    @BeforeEach
    void setUp() {
        Board board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        winner = new Player("A", 0);
        winner.addWorker(new Worker(board.getCellFromCoords(0, 0)));
        winner.addWorker(new Worker(board.getCellFromCoords(0, 1)));

        endGame = new EndGame(board, winner);
    }

    /**
     * Check that the current player is the winner
     */
    @Test
    void checkGetCurrentPlayer() {
        assertEquals(winner, endGame.getCurrentPlayer());
    }

    /**
     * Check that there is no next state
     */
    @Test
    void checkNextState() {
        assertEquals(endGame, endGame.nextState());
    }

    /**
     * Check that the game ends with this state
     */
    @Test
    void checkIsEnded() {
        assertTrue(endGame.isEnded());
    }

}