package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    public final static String DEFAULT_NAME_TEST = "gInton1c";
    public final static int DEFAULT_AGE_TEST = 42;

    private Board board;
    private Player player;

    @BeforeEach
    void setUp() {
        board = new Board();
        player = new Player(DEFAULT_NAME_TEST, DEFAULT_AGE_TEST);
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