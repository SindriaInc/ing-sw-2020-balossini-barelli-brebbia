package it.polimi.ingsw.model.gamestates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreInitGameTest {

    PreInitGame preInitGame;

    @BeforeEach
    void setup() {
        preInitGame = new PreInitGame(null);
    }

    @Test
    void testEmpty() {
        assertNull(preInitGame.getCurrentPlayer());
        assertEquals(preInitGame, preInitGame.nextState());
    }

}