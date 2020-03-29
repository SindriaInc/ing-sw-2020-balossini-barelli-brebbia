package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Board board;
    private Player player;

    @BeforeEach
    void setUp() {
        board = new Board();
        player = new Player(Player.DEFAULT_NAME, Player.DEFAULT_AGE);
    }

    @Test
    void checkHasWon() {
        Cell cell = board.getCellFromCoords(0, 1);
        cell.setLevel(3);

        Worker worker = new Worker(board.getCellFromCoords(0, 0));

        player.addWorker(worker);
        worker.move(cell);
        assertTrue(player.checkHasWon());
    }

}