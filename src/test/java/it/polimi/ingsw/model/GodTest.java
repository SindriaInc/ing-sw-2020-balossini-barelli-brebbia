package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GodTest {

    private final static String DEFAULT_NAME = "A";
    private final static int DEFAULT_ID = 1;
    private final static String DEFAULT_DESCRIPTION = "Standard";
    private final static String DEFAULT_TYPE = "Letter";

    private God god;

    @BeforeEach
    void setUp() {
        god = new God(DEFAULT_NAME, DEFAULT_ID, DEFAULT_DESCRIPTION, DEFAULT_TYPE);
    }

    @Test
    void testGetters() {
        assertEquals(god.getName(), DEFAULT_NAME);
        assertEquals(god.getId(), DEFAULT_ID);
        assertEquals(god.getDescription(), DEFAULT_DESCRIPTION);
        assertEquals(god.getType(), DEFAULT_TYPE);
    }

    // TODO: Test decorators

}