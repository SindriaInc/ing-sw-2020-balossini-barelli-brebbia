package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TurnTest {

    private Turn turn;

    @BeforeEach
    void setUp() {
        turn = new Turn();
    }

    @Test
    void testGetters() {
        assertEquals(turn.getBlocksPlaced(), new ArrayList<>());
        assertEquals(turn.getDomesPlaced(), new ArrayList<>());
    }

}