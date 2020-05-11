package it.polimi.ingsw.model;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.model.abilities.DefaultAbilities;
import it.polimi.ingsw.model.abilities.decorators.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GameTest {

    private static final String PLAYER_YOUNGEST_NAME = "A";
    private static final String PLAYER_MIDDLE_NAME = "B";
    private static final String PLAYER_OLDEST_NAME = "C";

    private Game game;
    private Deck deck;
    private List<Player> players;

    private int requestSpawnCount;
    private int winCount;

    /**
     * Check that there is no access to the inner model
     */
    @Test
    void checkGetBoard() {
        constructSimpleGame();
        game.init(players, deck, true);

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
        game.init(players, deck, true);
        assertEquals(ModelResponse.INVALID_STATE, game.selectGods(new ArrayList<>()));
    }

    /**
     * Check the simple getters
     */
    @Test
    void checkGetters() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("A", 1));
        players.add(new Player("B", 2));

        Game game = new Game(new ModelEventProvider());
        game.init(players, deck, true);

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
        game.getModelEventProvider().registerRequestWorkerSpawnEventObserver(event -> requestSpawnCount++);

        game.init(players, deck, false);

        List<God> gods = new ArrayList<>(deck.getGods());
        gods.remove(4);
        gods.remove(3);

        game.selectGods(godsToStringList(gods));
        game.chooseGod(gods.get(0).getName());
        game.chooseGod(gods.get(1).getName());
        assertEquals(requestSpawnCount, 0);
        game.chooseGod(gods.get(2).getName());

        // Check that we are in the next state
        assertEquals(requestSpawnCount, 1);
    }

    /**
     * Simulate a force situation
     */
    @Test
    void normalGameWithForce() {
        constructNormalGame();
        game.init(players, deck, false);

        List<God> gods = new ArrayList<>(deck.getGods());
        gods.remove(4);
        gods.remove(2);
        game.selectGods(godsToStringList(gods));
        game.chooseGod(gods.get(0).getName()); // BuildBeforeMove
        game.chooseGod(gods.get(1).getName()); // WinOnDeltaLevel
        game.chooseGod(gods.get(2).getName()); // ParkourCross

        game.spawnWorker(new Coordinates(1, 0)); // Worker 0
        game.spawnWorker(new Coordinates(3, 3)); // Worker 1
        game.spawnWorker(new Coordinates(4, 2)); // Worker 2
        game.spawnWorker(new Coordinates(2, 3)); // Worker 3
        game.spawnWorker(new Coordinates(4, 3)); // Worker 4
        game.spawnWorker(new Coordinates(2, 1)); // Worker 5

        assertEquals(ModelResponse.INVALID_PARAMS, game.moveWorker(1, new Coordinates(0, 0)));
        game.moveWorker(0, new Coordinates(0, 0));
        game.buildBlock(0, new Coordinates(1, 0));
        game.endTurn();

        game.moveWorker(3, new Coordinates(2, 2));
        game.buildBlock(3, new Coordinates(3, 2));
        game.endTurn();

        assertEquals(game.forceWorker(5, 3, new Coordinates(2, 0)), ModelResponse.ALLOW);
        assertEquals(game.forceWorker(4, 3, new Coordinates(2, 2)), ModelResponse.INVALID_PARAMS);
        game.moveWorker(5, new Coordinates(2, 2));
        game.buildBlock(5, new Coordinates(2, 1));
        game.endTurn();
    }

    /**
     * Simulate a simple game where two turns one player loses
     */
    @Test
    void simpleGameWithStates() {
        constructSimpleGame();
        game.getModelEventProvider().registerPlayerWinEventObserver(event -> {
            winCount++;
            assertEquals(event.getPlayer(), PLAYER_OLDEST_NAME);
        });
        game.init(players, deck, true);

        assertEquals(game.getCurrentPlayer().getName(), PLAYER_YOUNGEST_NAME);
        game.spawnWorker(new Coordinates(1, 0)); // Worker 0
        assertEquals(game.getCurrentPlayer().getName(), PLAYER_YOUNGEST_NAME);
        game.spawnWorker(new Coordinates(3, 3)); // Worker 1

        // Next turn
        assertEquals(game.getCurrentPlayer().getName(), PLAYER_OLDEST_NAME);
        game.spawnWorker(new Coordinates(4, 2)); // Worker 2
        assertEquals(game.getCurrentPlayer().getName(), PLAYER_OLDEST_NAME);
        game.spawnWorker(new Coordinates(2, 1)); // Worker 3

        game.moveWorker(0, new Coordinates(2, 0));
        assertEquals(ModelResponse.INVALID_STATE, game.endTurn());
        assertEquals(ModelResponse.INVALID_PARAMS, game.buildDome(0, new Coordinates(3, 0)));
        game.buildBlock(0, new Coordinates(3, 0));
        game.endTurn();

        getCell(game, 3, 2).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        assertEquals(ModelResponse.INVALID_PARAMS, game.moveWorker(3, new Coordinates(2, 0)));
        game.moveWorker(2, new Coordinates(4, 3));
        assertEquals(ModelResponse.INVALID_STATE, game.endTurn());
        game.buildDome(2, new Coordinates(3, 2));
        game.endTurn();

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
        assertEquals(winCount, 0);
        game.endTurn();
        assertEquals(winCount, 1);
    }

    private Cell getCell(Game game, int x, int y) {
        return game.getOriginalBoard().getCellFromCoords(x, y);
    }

    private void constructSimpleGame() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(PLAYER_YOUNGEST_NAME, 1));
        players.add(new Player(PLAYER_OLDEST_NAME, 2));

        this.game = new Game(new ModelEventProvider());
        this.players = players;
    }

    private void constructNormalGame() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(PLAYER_YOUNGEST_NAME, 1));
        players.add(new Player(PLAYER_MIDDLE_NAME, 2));
        players.add(new Player(PLAYER_OLDEST_NAME, 3));

        List<God> gods = new ArrayList<>();
        gods.add(new God("G1", 1, "", "","", Map.of(BuildBeforeMove.class, false)));
        gods.add(new God("G2", 2, "", "","", Map.of(WinOnDeltaLevel.class, false)));
        gods.add(new God("G3", 3, "", "","", Map.of(BlockOnPlayerMoveUp.class, true)));
        gods.add(new God("G4", 4, "", "","", Map.of(ParkourCross.class, false)));
        gods.add(new God("G5", 5, "", "","", Map.of(NoWinOnPerimeter.class, true)));
        deck = new Deck(gods);

        this.game = new Game(new ModelEventProvider());
        this.players = players;
    }
    private List<String> godsToStringList(List<God> gods) {
        return gods.stream().map(God::getName).collect(Collectors.toList());
    }

}