package it.polimi.ingsw.model;

import it.polimi.ingsw.model.abilities.DefaultAbilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Board board;
    private Player player;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        player = new Player(TestConstants.PLAYER_NAME, TestConstants.PLAYER_AGE);
    }

    @Test
    void testGetters() {
        assertEquals(player.getWorkers(), new ArrayList<>());
        assertEquals(player.getName(), TestConstants.PLAYER_NAME);
        assertEquals(player.getAge(), TestConstants.PLAYER_AGE);
    }

}