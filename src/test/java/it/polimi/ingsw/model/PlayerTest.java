package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}