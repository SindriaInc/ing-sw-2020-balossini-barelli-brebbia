package it.polimi.ingsw.model;

import it.polimi.ingsw.model.abilities.DefaultAbilities;
import it.polimi.ingsw.model.abilities.decorators.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.TestConstants.equalsNoOrder;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static final String PLAYER_YOUNGEST_NAME = "A";
    private static final String PLAYER_MIDDLE_NAME = "B";
    private static final String PLAYER_OLDEST_NAME = "C";

    private Game game;
    private List<Player> players;

    /**
     * Check that there is no access to the inner model
     */
    @Test
    void checkGetBoard() {
        constructSimpleGame();

        Board board = game.getBoard();
        board.getCellFromCoords(0, 0).setLevel(1);
        board.getCellFromCoords(0, 0).setDoomed(true);

        assertEquals(0, game.getBoard().getCellFromCoords(0, 0).getLevel());
        assertFalse(game.getBoard().getCellFromCoords(0, 0).isDoomed());
    }

    /**
     * Check that the simple game rule throws an exception for the God choice in a simple game
     */
    @Test
    void checkSimpleGame() {
        constructSimpleGame();
        assertEquals(Game.ModelResponse.INVALID_STATE, game.selectGods(new ArrayList<>()));
    }

    /**
     * Check the simple getters
     */
    @Test
    void checkGetters() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("A", 1));
        players.add(new Player("B", 2));

        Game game = new Game(players, null, true);

        List<Player> opponents = new ArrayList<>(players);
        Player player = opponents.get(0);
        opponents.remove(player);

        assertEquals(game.getCurrentPlayer(), players.get(0)); // In a simple game the first player is the youngest one
    }

    /**
     * Check god selection -> workers selection
     */
    @Test
    void checkGodSelection() {
        constructNormalGame();

        List<God> gods = new ArrayList<>(game.getAvailableGods());
        gods.remove(4);
        gods.remove(3);

        assertEquals(game.getSelectGodsCount(), players.size());
        assertEquals(game.getSelectGodsCount(), players.size());
        assertTrue(game.checkCanSelectGods(gods));
        game.selectGods(gods);

        assertTrue(equalsNoOrder(gods, game.getAvailableGods()));
        game.chooseGod(gods.get(0));
        game.chooseGod(gods.get(1));
        game.chooseGod(gods.get(2));

        // Check that we are in the next state
        assertNull(game.getAvailableGods());
        assertTrue(game.getAvailableCells().size() > 0);
    }

    /**
     * Simulate a force situation
     */
    @Test
    void normalGameWithForce() {
        constructNormalGame();

        List<God> gods = new ArrayList<>(game.getAvailableGods());
        gods.remove(4);
        gods.remove(2);
        game.selectGods(gods);
        game.chooseGod(gods.get(0)); // BuildBeforeMove
        game.chooseGod(gods.get(1)); // WinOnDeltaLevel
        game.chooseGod(gods.get(2)); // ParkourCross

        Worker player1worker1 = new Worker(getCell(game, 1, 0));
        Worker player1worker2 = new Worker(getCell(game, 3, 3));
        Worker player2worker1 = new Worker(getCell(game, 4, 2));
        Worker player2worker2 = new Worker(getCell(game, 2, 3));
        Worker player3worker1 = new Worker(getCell(game, 4, 3));
        Worker player3worker2 = new Worker(getCell(game, 2, 1));

        game.spawnWorker(player1worker1);
        game.spawnWorker(player1worker2);
        game.spawnWorker(player2worker1);
        game.spawnWorker(player2worker2);
        game.spawnWorker(player3worker1);
        game.spawnWorker(player3worker2);

        assertFalse(game.checkCanEndTurn());
        game.moveWorker(player1worker1, getCell(game, 0, 0));
        game.buildBlock(player1worker1, getCell(game, 1, 0));
        game.endTurn();

        game.moveWorker(player2worker2, getCell(game, 2, 2));
        game.buildBlock(player2worker2, getCell(game, 3, 2));
        game.endTurn();

        assertTrue(equalsNoOrder(game.getAvailableForces(player3worker2, player2worker2), List.of(getCell(game, 2, 0))));
        game.forceWorker(player3worker2, player2worker2, getCell(game, 2, 0));
        game.moveWorker(player3worker2, getCell(game, 2, 2));
        game.buildBlock(player3worker2, getCell(game, 2, 1));
        game.endTurn();

        getCell(game, 1, 0).setLevel(3);
        getCell(game, 0, 1).setLevel(3);
        getCell(game, 1, 1).setLevel(3);
        assertFalse(game.checkCanEndTurn());
    }

    /**
     * Simulate a simple game where two turns one player loses
     */
    @Test
    void simpleGameWithStates() {
        constructSimpleGame();

        Worker player1worker1 = new Worker(getCell(game, 1, 0));
        Worker player1worker2 = new Worker(getCell(game, 3, 3));
        Worker player2worker1 = new Worker(getCell(game, 4, 2));
        Worker player2worker2 = new Worker(getCell(game, 2, 1));
        Worker unusedWorker = new Worker(getCell(game, 4, 2));

        assertEquals(game.getCurrentPlayer().getName(), PLAYER_YOUNGEST_NAME);
        game.spawnWorker(player1worker1);
        assertEquals(game.getCurrentPlayer().getName(), PLAYER_YOUNGEST_NAME);
        game.spawnWorker(player1worker2);

        // Next turn
        assertEquals(game.getCurrentPlayer().getName(), PLAYER_OLDEST_NAME);
        game.spawnWorker(player2worker1);
        assertEquals(game.getCurrentPlayer().getName(), PLAYER_OLDEST_NAME);
        assertEquals(Game.ModelResponse.INVALID_PARAMS,game.spawnWorker(unusedWorker));
        game.spawnWorker(player2worker2);

        assertTrue(game.getAvailableMoves(player1worker1).contains(getCell(game, 2, 0)));
        game.moveWorker(player1worker1, getCell(game, 2, 0));
        assertEquals(Game.ModelResponse.INVALID_STATE, game.endTurn());
        assertEquals(Game.ModelResponse.INVALID_PARAMS, game.buildDome(player1worker1,getCell(game, 3, 0)));
        assertTrue(game.getAvailableBlockBuilds(player1worker1).contains(getCell(game, 3, 0)));
        game.buildBlock(player1worker1, getCell(game, 3, 0));
        game.endTurn();

        getCell(game, 3, 2).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertEquals(Game.ModelResponse.INVALID_PARAMS, game.moveWorker(player2worker2,getCell(game, 2, 0)));
        game.moveWorker(player2worker1, getCell(game, 4, 3));
        assertEquals(Game.ModelResponse.INVALID_STATE, game.endTurn());
        assertTrue(game.getAvailableDomeBuilds(player2worker1).contains(getCell(game, 3, 2)));
        game.buildDome(player2worker1, getCell(game, 3, 2));
        game.endTurn();

        assertFalse(game.checkCanEndTurn());
        getCell(game, 1, 0).setLevel(2);
        getCell(game, 1, 1).setLevel(2);
        getCell(game, 2, 1).setLevel(2);
        getCell(game, 2, 2).setLevel(2);
        getCell(game, 3, 0).setLevel(2);
        getCell(game, 2, 3).setLevel(2);
        getCell(game, 2, 4).setLevel(2);
        getCell(game, 3, 4).setLevel(2);
        getCell(game, 4, 4).setLevel(2);
        getCell(game, 4, 3).setLevel(2);
        getCell(game, 4, 2).setLevel(2);
        getCell(game, 3, 2).setLevel(2);
        getCell(game, 3, 1).setLevel(2);
        Player toRemove = game.getCurrentPlayer();
        assertTrue(game.checkCanEndTurn());
        game.endTurn();
        assertTrue(game.checkHasLost(toRemove));
        assertTrue(game.isEnded());
    }

    private Cell getCell(Game game, int x, int y) {
        return game.getOriginalBoard().getCellFromCoords(x, y);
    }

    private void constructSimpleGame() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(PLAYER_YOUNGEST_NAME, 1));
        players.add(new Player(PLAYER_OLDEST_NAME, 2));

        this.game = new Game(players, null, true);
        this.players = players;
    }

    private void constructNormalGame() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(PLAYER_YOUNGEST_NAME, 1));
        players.add(new Player(PLAYER_MIDDLE_NAME, 2));
        players.add(new Player(PLAYER_OLDEST_NAME, 3));

        List<God> gods = new ArrayList<>();
        gods.add(new God("G1", 1, "", "", Map.of(BuildBeforeMove.class, false)));
        gods.add(new God("G2", 2, "", "", Map.of(WinOnDeltaLevel.class, false)));
        gods.add(new God("G3", 3, "", "", Map.of(BlockOnPlayerMoveUp.class, true)));
        gods.add(new God("G4", 4, "", "", Map.of(ParkourCross.class, false)));
        gods.add(new God("G5", 5, "", "", Map.of(NoWinOnPerimeter.class, true)));
        Deck deck = new Deck(gods);

        this.game = new Game(players, deck, false);
        this.players = players;
    }

}