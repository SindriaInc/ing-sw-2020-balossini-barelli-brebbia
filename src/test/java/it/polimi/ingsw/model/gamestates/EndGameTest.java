package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TestConstants;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EndGameTest {

    private EndGame endGame;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        Board board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        players = List.of(new Player("A", 0));
        players.get(0).addWorker(new Worker(board.getCellFromCoords(0, 0)));
        players.get(0).addWorker(new Worker(board.getCellFromCoords(0, 1)));

        endGame = new EndGame(board, players);
    }

    @Test
    void checkGetCurrentPlayer() {
        // No player can play after the game ends
        assertNull(endGame.getCurrentPlayer());
    }

    @Test
    void checkNextState() {
        // No state after the end state
        assertEquals(endGame, endGame.nextState());
    }

}