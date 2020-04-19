package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(TestConstants.PLAYER_NAME, TestConstants.PLAYER_AGE);
    }

    @Test
    void testGetters() {
        assertEquals(player.getWorkers(), new ArrayList<>());
        assertEquals(player.getName(), TestConstants.PLAYER_NAME);
        assertEquals(player.getAge(), TestConstants.PLAYER_AGE);
    }

    /**
     * Check that a worker cannot be added more than once
     */
    @Test
    void testAddWorker() {
        Cell cell = new Cell(0, 0);
        Worker worker = new Worker(cell);
        player.addWorker(worker);

        assertThrows(IllegalArgumentException.class, () -> player.addWorker(worker));
    }

    /**
     * Check if a player has won or not
     */
    @Test
    void testCheckHasWon() {
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(0, 3);
        Cell cell3 = new Cell(0, 1);
        Worker worker = new Worker(cell1);
        Worker enemyWorker = new Worker(cell2);
        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(enemyWorker, false);
        Turn turn = new Turn(worker, otherWorkers, (cell) -> Arrays.asList(cell, cell1), cell -> false);

        assertFalse(player.checkHasWon(turn));

        cell2.setLevel(3);

        assertFalse(player.checkHasWon(turn));

        cell3.setLevel(3);
        worker.move(cell3);

        assertTrue(player.checkHasWon(turn));
    }

}