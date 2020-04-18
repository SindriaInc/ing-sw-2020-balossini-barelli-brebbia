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

    /**
     * Check that the simple game rule throws an exception for the God choice in a simple game
     */
    @Test
    void checkSimpleGame() {
        Game game = constructSimpleGame();
        assertThrows(IllegalStateException.class, () -> game.selectGods(new ArrayList<>()));
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

        assertEquals(game.getPlayers(), players);
        assertEquals(game.getOpponents(player), opponents);
        assertEquals(game.getCurrentPlayer(), players.get(0)); // In a simple game the first player is the youngest one
    }

    /**
     * Check god selection -> workers selection
     */
    @Test
    void checkGodSelection() {
        Game game = constructNormalGame();

        List<God> gods = new ArrayList<>(game.getAvailableGods());
        gods.remove(4);
        gods.remove(3);

        assertEquals(game.getSelectGodsCount(), game.getPlayers().size());
        assertTrue(game.checkCanSelectGods(gods));
        game.selectGods(gods);

        assertTrue(equalsNoOrder(gods, game.getAvailableGods()));
        game.chooseGod(gods.get(0));
        game.chooseGod(gods.get(1));
        game.chooseGod(gods.get(2));

        // Check that we are in the next state
        assertThrows(IllegalStateException.class, game::getAvailableGods);
        assertTrue(game.getAvailableCells().size() > 0);
    }

    /**
     * Simulate a force situation
     */
    @Test
    void normalGameWithForce() {
        Game game = constructNormalGame();

        List<God> gods = new ArrayList<>(game.getAvailableGods());
        gods.remove(4);
        gods.remove(2);
        game.selectGods(gods);
        game.chooseGod(gods.get(0)); // AdditionalBuildOnSameCell
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

        game.moveWorker(player1worker1, getCell(game, 0, 0));
        game.buildBlock(player1worker1, getCell(game, 1, 0));
        game.endTurn();

        game.moveWorker(player2worker2, getCell(game, 2, 2));
        game.buildBlock(player2worker2, getCell(game, 3, 2));
        game.endTurn();

        assertTrue(equalsNoOrder(game.getAvailableForces(player3worker2, player2worker2), List.of(getCell(game, 2, 0))));
        game.forceWorker(player3worker2, player2worker2, getCell(game, 2, 0));
        assertEquals(game.getCurrentPlayer(), game.getPlayers().get(2));
    }

    /**
     * Simulate a simple game where two turns one player loses
     */
    @Test
    void simpleGameWithStates() {
        Game game = constructSimpleGame();

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
        assertThrows(IllegalArgumentException.class, () -> game.spawnWorker(unusedWorker));
        game.spawnWorker(player2worker2);

        assertTrue(game.getAvailableMoves(player1worker1).contains(getCell(game, 2, 0)));
        game.moveWorker(player1worker1, getCell(game, 2, 0));
        assertThrows(IllegalStateException.class, game::endTurn);
        assertThrows(IllegalArgumentException.class, () -> game.buildDome(player1worker1,getCell(game, 3, 0)));
        assertTrue(game.getAvailableBlockBuilds(player1worker1).contains(getCell(game, 3, 0)));
        game.buildBlock(player1worker1, getCell(game, 3, 0));
        game.endTurn();

        getCell(game, 3, 2).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertThrows(IllegalArgumentException.class, () -> game.moveWorker(player2worker2,getCell(game, 2, 0)));
        game.moveWorker(player2worker1, getCell(game, 4, 3));
        assertThrows(IllegalStateException.class, game::endTurn);
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
        assertFalse(game.getPlayers().contains(toRemove));
    }

    private Cell getCell(Game game, int x, int y) {
        return game.getBoard().getCellFromCoords(x, y);
    }

    private Game constructSimpleGame() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(PLAYER_YOUNGEST_NAME, 1));
        players.add(new Player(PLAYER_OLDEST_NAME, 2));

        return new Game(players, null, true);
    }

    private Game constructNormalGame() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(PLAYER_YOUNGEST_NAME, 1));
        players.add(new Player(PLAYER_MIDDLE_NAME, 2));
        players.add(new Player(PLAYER_OLDEST_NAME, 3));

        List<God> gods = new ArrayList<>();
        gods.add(new God("G1", 1, "", "", Map.of(AdditionalBuildOnSameCell.class, false)));
        gods.add(new God("G2", 2, "", "", Map.of(WinOnDeltaLevel.class, false)));
        gods.add(new God("G3", 3, "", "", Map.of(BlockOnPlayerMoveUp.class, true)));
        gods.add(new God("G4", 4, "", "", Map.of(ParkourCross.class, false)));
        gods.add(new God("G5", 5, "", "", Map.of(NoWinOnPerimeter.class, true)));
        Deck deck = new Deck(gods);

        return new Game(players, deck, false);
    }

}