package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TestConstants;
import it.polimi.ingsw.model.abilities.decorators.AdditionalMove;
import it.polimi.ingsw.model.abilities.decorators.BuildBeforeMove;
import it.polimi.ingsw.model.abilities.decorators.BuildBelow;
import it.polimi.ingsw.model.abilities.decorators.ParkourCross;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PreGodsGameTest {

    private PreGodsGame preGodsGame;
    private List<God> gods;
    God god4;
    private int playerCount;

    @BeforeEach
    void setUp() {
        Board board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        List<Player> players = List.of(new Player("A", 0), new Player("B", 1));
        God god1 = new God("G1", 1, "", "", Map.of(AdditionalMove.class, false));
        God god2 = new God("G2", 2, "", "", Map.of(BuildBeforeMove.class, false));
        God god3 = new God("G3", 3, "", "", Map.of(BuildBelow.class, false));
        gods = List.of(god1, god2, god3);
        playerCount = players.size();

        preGodsGame = new PreGodsGame(board, players, TestConstants.MAX_WORKERS, gods);

        god4 = new God("G3", 4, "", "", Map.of(ParkourCross.class, false));
    }

    /**
     * Check the list of gods
     */
    @Test
    void checkGetAvailableGods() {
        assertEquals(preGodsGame.getAvailableGods(), gods);

        preGodsGame.selectGods(List.of(gods.get(0), gods.get(1)));

        assertEquals(preGodsGame.getAvailableGods(), List.of(gods.get(0), gods.get(1)));

        preGodsGame.chooseGod(gods.get(1));

        assertEquals(preGodsGame.getAvailableGods(), List.of(gods.get(0)));

        preGodsGame.chooseGod(gods.get(0));

        assertEquals(preGodsGame.getAvailableGods(), List.of());
    }

    /**
     * Check the number of player to assign the god cards
     */
    @Test
    void checkGetSelectGodsCount() {
        assertEquals(preGodsGame.getSelectGodsCount(), playerCount);
    }

    /**
     * Check the correct selection of gods
     */
    @Test
    void checkCanSelectGods() {
        assertTrue(preGodsGame.checkCanSelectGods(List.of(gods.get(0), gods.get(1))));
        assertTrue(preGodsGame.checkCanSelectGods(List.of(gods.get(1), gods.get(2))));
        assertFalse(preGodsGame.checkCanSelectGods(List.of(gods.get(0))));
        assertFalse(preGodsGame.checkCanSelectGods(List.of(gods.get(0), gods.get(1), gods.get(2))));
        assertFalse(preGodsGame.checkCanSelectGods(List.of(gods.get(0), gods.get(0))));
        assertFalse(preGodsGame.checkCanSelectGods(List.of(gods.get(0), god4)));
    }

    /**
     * Check that can't select gods after a selection has already been done
     */
    @Test
    void checkSelectGods() {
        assertEquals(preGodsGame, preGodsGame.nextState());

        assertThrows(IllegalArgumentException.class, () -> preGodsGame.selectGods(List.of()));

        preGodsGame.selectGods(List.of(gods.get(0), gods.get(1)));

        assertEquals(preGodsGame, preGodsGame.nextState());
        assertEquals(preGodsGame.getPlayers().get(0), preGodsGame.getCurrentPlayer());
        assertEquals(preGodsGame.getAvailableGods(), List.of(gods.get(0), gods.get(1)));

        assertThrows(IllegalStateException.class, () -> preGodsGame.selectGods(List.of()));
    }

    /**
     * Check that a player can't choose a god before the challenger chooses the available gods, the correct selection
     * and that after each player has chosen a god, the state should change
     */
    @Test
    void checkChooseGod() {
        assertThrows(IllegalStateException.class, () -> preGodsGame.chooseGod(gods.get(0)));

        preGodsGame.selectGods(List.of(gods.get(0), gods.get(1)));

        preGodsGame.chooseGod(gods.get(1));

        assertEquals(preGodsGame, preGodsGame.nextState());
        assertEquals(preGodsGame.getPlayers().get(1), preGodsGame.getCurrentPlayer());
        assertEquals(preGodsGame.getAvailableGods(), List.of(gods.get(0)));

        preGodsGame.chooseGod(gods.get(0));

        assertNotEquals(preGodsGame, preGodsGame.nextState());
        assertEquals(preGodsGame.getAvailableGods(), List.of());

        assertThrows(IllegalArgumentException.class, () -> preGodsGame.chooseGod(god4));
    }

    /**
     * Check that the active player in CHALLENGER_SELECT_GODS phase is the challenger
     */
    @Test
    void checkCurrentPlayer() {
        assertEquals(preGodsGame.getCurrentPlayer(), preGodsGame.getPlayers().get(1));
    }

}