package it.polimi.ingsw.model;

import it.polimi.ingsw.model.abilities.DefaultAbilities;
import it.polimi.ingsw.model.abilities.IAbilities;
import it.polimi.ingsw.model.abilities.decorators.AdditionalBuildOnDifferentCell;
import it.polimi.ingsw.model.abilities.decorators.AdditionalBuildOnSameCell;
import it.polimi.ingsw.model.abilities.decorators.BlockOnPlayerMoveUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GodTest {

    private final static String DEFAULT_NAME = "A";
    private final static int DEFAULT_ID = 1;
    private final static String DEFAULT_TITLE = "Title";
    private final static String DEFAULT_DESCRIPTION = "Standard";
    private final static String DEFAULT_TYPE = "Letter";

    private God god;

    @BeforeEach
    void setUp() {
        god = new God(DEFAULT_NAME, DEFAULT_ID, DEFAULT_TITLE, DEFAULT_DESCRIPTION, DEFAULT_TYPE, Map.of());
    }

    @Test
    void testGetters() {
        assertEquals(god.getName(), DEFAULT_NAME);
        assertEquals(god.getId(), DEFAULT_ID);
        assertEquals(god.getTitle(), DEFAULT_TITLE);
        assertEquals(god.getDescription(), DEFAULT_DESCRIPTION);
        assertEquals(god.getType(), DEFAULT_TYPE);
    }

    @Test
    void testNoAbilities() {
        IAbilities abilities = new DefaultAbilities();
        abilities = god.applyAbilities(abilities);

        assertTrue(abilities instanceof DefaultAbilities);
    }

    @Test
    void testApplyAbilities() {
        god = new God(DEFAULT_NAME, DEFAULT_ID, DEFAULT_TITLE, DEFAULT_DESCRIPTION, DEFAULT_TYPE, Map.of(AdditionalBuildOnDifferentCell.class, false));

        IAbilities abilities = new DefaultAbilities();
        abilities = god.applyAbilities(abilities);

        assertFalse(abilities instanceof DefaultAbilities);
        assertTrue(abilities instanceof AdditionalBuildOnDifferentCell);

        abilities = new DefaultAbilities();
        abilities = god.applyOpponentAbilities(abilities, new Player(TestConstants.PLAYER_NAME, TestConstants.PLAYER_AGE));

        assertTrue(abilities instanceof DefaultAbilities);
    }

    @Test
    void testApplyOpponentAbilities() {
        god = new God(DEFAULT_NAME, DEFAULT_ID, DEFAULT_TITLE, DEFAULT_DESCRIPTION, DEFAULT_TYPE, Map.of(BlockOnPlayerMoveUp.class, true));

        IAbilities abilities = new DefaultAbilities();
        Player player = new Player(TestConstants.PLAYER_NAME, TestConstants.PLAYER_AGE);
        abilities = god.applyOpponentAbilities(abilities, player);

        assertFalse(abilities instanceof DefaultAbilities);
        assertTrue(abilities instanceof BlockOnPlayerMoveUp);

        abilities = new DefaultAbilities();
        abilities = god.applyAbilities(abilities);

        assertTrue(abilities instanceof DefaultAbilities);
    }

    @Test
    void testInvalidEffect() {
        god = new God(DEFAULT_NAME, DEFAULT_ID, DEFAULT_TITLE, DEFAULT_DESCRIPTION, DEFAULT_TYPE, Map.of(
                BlockOnPlayerMoveUp.class, false,
                AdditionalBuildOnSameCell.class, true
        ));

        IAbilities abilities = new DefaultAbilities();
        Player player = new Player(TestConstants.PLAYER_NAME, TestConstants.PLAYER_AGE);

        assertThrows(IllegalStateException.class, () -> god.applyAbilities(abilities));
        assertThrows(IllegalStateException.class, () -> god.applyOpponentAbilities(abilities, player));
    }

}