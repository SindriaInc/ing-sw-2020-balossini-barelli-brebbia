package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.info.GodInfo;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.abilities.decorators.AdditionalMove;
import it.polimi.ingsw.model.abilities.decorators.BuildBeforeMove;
import it.polimi.ingsw.model.abilities.decorators.BuildBelow;
import it.polimi.ingsw.model.abilities.decorators.ParkourCross;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.TestConstants.equalsNoOrder;
import static org.junit.jupiter.api.Assertions.*;

class PreGodsGameTest {

    private PreGodsGame preGodsGame;
    private List<God> gods;
    private Board board;
    private List<Player> players;
    private God god4;
    private int playerCount;
    private ModelEventProvider modelEventProvider;

    private int requestSelectCount;
    private int requestChooseCount;
    private int selectCount;
    private int chooseCount;
    private int turnStartCount;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
        players = List.of(new Player("A", 0), new Player("B", 1));
        God god1 = new God("G1", 1, "", "", "", Map.of(AdditionalMove.class, false));
        God god2 = new God("G2", 2, "", "", "", Map.of(BuildBeforeMove.class, false));
        God god3 = new God("G3", 3, "", "", "", Map.of(BuildBelow.class, false));
        gods = List.of(god1, god2, god3);
        playerCount = players.size();
        modelEventProvider = new ModelEventProvider();
        god4 = new God("G3", 4, "", "", "", Map.of(ParkourCross.class, false));
    }

    /**
     * Check the list of gods
     */
    @Test
    void checkGetAvailableGods() {
        modelEventProvider.registerPlayerTurnStartEventObserver(event -> turnStartCount++);

        modelEventProvider.registerRequestPlayerChallengerSelectGodsEventObserver(event -> {
            assertEquals(event.getSelectedGodsCount(), playerCount);
            List<GodInfo> godInfos = godsToGodInfoList(gods);
            assertTrue(equalsNoOrder(event.getGods(), godInfos));
            requestSelectCount++;
        });

        List<String> selectedGods = List.of(gods.get(0).getName(), gods.get(1).getName());
        preGodsGame = new PreGodsGame(modelEventProvider, board, players, TestConstants.MAX_WORKERS, gods);
        assertEquals(turnStartCount, 1);
        assertEquals(requestSelectCount, 1);

        modelEventProvider.registerPlayerChallengerSelectGodsEventObserver(event -> {
            assertTrue(equalsNoOrder(event.getGods(), selectedGods));
            selectCount++;
        });

        modelEventProvider.registerRequestPlayerChooseGodEventObserver(event -> {
            if (requestChooseCount == 0) {
                assertTrue(equalsNoOrder(godInfoToStringList(event.getAvailableGods()), selectedGods));
            } else if (requestChooseCount == 1) {
                assertTrue(equalsNoOrder(godInfoToStringList(event.getAvailableGods()), List.of(gods.get(0).getName())));
            }
            requestChooseCount++;
        });

        assertEquals(ModelResponse.INVALID_PARAMS, preGodsGame.selectGods(List.of("NotPresent")));
        assertEquals(ModelResponse.INVALID_PARAMS, preGodsGame.selectGods(List.of(gods.get(0).getName(), gods.get(0).getName())));
        preGodsGame.selectGods(selectedGods);
        assertEquals(turnStartCount, 2);
        assertEquals(requestChooseCount, 1);
        assertEquals(selectCount, 1);

        modelEventProvider.registerPlayerChooseGodEventObserver(event -> {
            if (chooseCount == 0) {
                assertEquals(event.getGod(), gods.get(1).getName());
            } else if (chooseCount == 1) {
                assertEquals(event.getGod(), gods.get(0).getName());
            }
            chooseCount++;
        });

        preGodsGame.chooseGod(gods.get(1).getName());
        assertEquals(turnStartCount, 3);
        assertEquals(requestChooseCount, 2);
        assertEquals(chooseCount, 1);
        preGodsGame.chooseGod(gods.get(0).getName());
        // Goes into the next state
        assertEquals(chooseCount, 2);
        assertEquals(requestChooseCount, 2);
        assertEquals(selectCount, 1);
        assertEquals(requestSelectCount, 1);
        assertNotEquals(preGodsGame, preGodsGame.nextState());
    }

    /**
     * Check that can't select gods after a selection has already been done
     */
    @Test
    void checkSelectGods() {
        preGodsGame = new PreGodsGame(modelEventProvider, board, players, TestConstants.MAX_WORKERS, gods);
        assertEquals(preGodsGame, preGodsGame.nextState());

        assertEquals(ModelResponse.INVALID_PARAMS,preGodsGame.selectGods(List.of()));

        preGodsGame.selectGods(List.of(gods.get(0).getName(), gods.get(1).getName()));

        assertEquals(preGodsGame, preGodsGame.nextState());
        assertEquals(preGodsGame.getPlayers().get(0), preGodsGame.getCurrentPlayer());

        assertEquals(ModelResponse.INVALID_STATE, preGodsGame.selectGods(List.of()));
    }

    /**
     * Check that a player can't choose a god before the challenger chooses the available gods, the correct selection
     * and that after each player has chosen a god, the state should change
     */
    @Test
    void checkChooseGod() {
        preGodsGame = new PreGodsGame(modelEventProvider, board, players, TestConstants.MAX_WORKERS, gods);
        assertEquals(ModelResponse.INVALID_STATE, preGodsGame.chooseGod(gods.get(0).getName()));

        preGodsGame.selectGods(List.of(gods.get(0).getName(), gods.get(1).getName()));

        preGodsGame.chooseGod(gods.get(1).getName());

        assertEquals(preGodsGame, preGodsGame.nextState());
        assertEquals(preGodsGame.getPlayers().get(1), preGodsGame.getCurrentPlayer());

        preGodsGame.chooseGod(gods.get(0).getName());

        assertNotEquals(preGodsGame, preGodsGame.nextState());

        assertEquals(ModelResponse.INVALID_PARAMS, preGodsGame.chooseGod(god4.getName()));
    }

    /**
     * Check that the active player in CHALLENGER_SELECT_GODS phase is the challenger
     */
    @Test
    void checkCurrentPlayer() {
        preGodsGame = new PreGodsGame(modelEventProvider, board, players, TestConstants.MAX_WORKERS, gods);
        assertEquals(preGodsGame.getCurrentPlayer(), preGodsGame.getPlayers().get(1));
    }

    private List<String> godInfoToStringList(List<GodInfo> gods) {
        return gods.stream().map(GodInfo::getName).collect(Collectors.toList());
    }

    private List<GodInfo> godsToGodInfoList(List<God> gods) {
        return gods.stream().map(god -> new GodInfo(god.getName(), god.getId(), god.getTitle(), god.getDescription(), god.getType())).collect(Collectors.toList());
    }

}