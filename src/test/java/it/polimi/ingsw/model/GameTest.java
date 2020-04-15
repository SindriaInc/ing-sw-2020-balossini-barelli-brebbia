package it.polimi.ingsw.model;

import it.polimi.ingsw.model.gamestates.OngoingGame;
import it.polimi.ingsw.model.gamestates.PreGodsGame;
import it.polimi.ingsw.model.gamestates.PreWorkersGame;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.model.TestConstants.equalsNoOrder;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    /**
     * Check that the simple game rule throws an exception for the God choice in a simple game
     */
    @Test
    void checkSimpleGame() {
        Game game = new Game(List.of(new Player("A", 0), new Player("B", 1)), true);
        assertThrows(IllegalStateException.class, () -> game.selectGods(new ArrayList<>()));
    }

    /**
     * Check the get opponent method
     */
    @Test
    void checkGetOpponents() {
        Game game = constructNormalGame();

        List<Player> opponents = game.getPlayers();
        Player player = opponents.get(0);
        opponents.remove(player);

        assertEquals(game.getOpponents(player), opponents);
    }

    /**
     * Check the getAvailableGod method
     */
    @Test
    void checkGetAvailableGods() {
        Game game = constructNormalGame();

        List<God> availableGods = game.getAvailableGods();
        game.chooseGod(availableGods.get(0));
        availableGods.remove(0);

        assertEquals(availableGods, game.getAvailableGods());
    }

    /**
     * Check the getAvailableMoves method with an angular worker, a side one and an inner one.
     * Later put constraints in movement like neighbor workers, tall towers and domes
     */
    @Test
    void checkGetAvailableMoves() {
        Game game = constructNormalGame();

        Worker worker1 = new Worker(getCell(game, 0,0));
        Worker worker2 = new Worker(getCell(game, 2,0));
        Worker worker3 = new Worker(getCell(game, 3,3));

        game.spawnWorker(worker1);
        game.spawnWorker(worker2);
        game.spawnWorker(worker3);

        assertTrue(() -> equalsNoOrder(
                game.getAvailableMoves(worker1),
                Arrays.asList(
                        getCell(game, 1, 0),
                        getCell(game, 1, 1),
                        getCell(game, 0, 1)
                )
        ));

        assertTrue(() -> equalsNoOrder(
                game.getAvailableMoves(worker2),
                Arrays.asList(
                        getCell(game, 1, 0),
                        getCell(game, 1, 1),
                        getCell(game, 2, 1),
                        getCell(game, 3, 1),
                        getCell(game, 3, 0)
                )
        ));

        assertTrue(() -> equalsNoOrder(
                game.getAvailableMoves(worker3),
                Arrays.asList(
                        getCell(game, 2, 2),
                        getCell(game, 2, 3),
                        getCell(game, 2, 4),
                        getCell(game, 3, 2),
                        getCell(game, 3, 4),
                        getCell(game, 4, 2),
                        getCell(game, 4, 3),
                        getCell(game, 4, 4)
                )
        ));

        Worker worker4 = new Worker(getCell(game, 4,4));
        game.spawnWorker(worker4);

        getCell(game, 4, 3).setLevel(1);
        getCell(game, 4, 3).setLevel(2);
        getCell(game, 4, 2).setDoomed(true);

        assertTrue(() -> equalsNoOrder(
                game.getAvailableMoves(worker2),
                Arrays.asList(
                        getCell(game, 2, 2),
                        getCell(game, 2, 3),
                        getCell(game, 2, 4),
                        getCell(game, 3, 2),
                        getCell(game, 3, 4)
                )
        ));
    }

    /**
     * Check the getAvailableBlockBuilds method with an angular worker, a side one and an inner one.
     * Later put constraints in movement like neighbor workers and domes
     */
    @Test
    void checkGetAvailableBlockBuilds() {
        Game game = constructNormalGame();

        Worker worker1 = new Worker(getCell(game, 0,0));
        Worker worker2 = new Worker(getCell(game, 2,0));
        Worker worker3 = new Worker(getCell(game, 3,3));

        game.spawnWorker(worker1);
        game.spawnWorker(worker2);
        game.spawnWorker(worker3);

        assertTrue(() -> game.getAvailableBlockBuilds(worker1).equals(
                Arrays.asList(
                        getCell(game, 1, 0),
                        getCell(game, 1, 1),
                        getCell(game, 0, 1)
                )
        ));

        assertTrue(() -> game.getAvailableBlockBuilds(worker2).equals(
                Arrays.asList(
                        getCell(game, 1, 0),
                        getCell(game, 1, 1),
                        getCell(game, 2, 1),
                        getCell(game, 3, 1),
                        getCell(game, 3, 0)
                )
        ));

        assertTrue(() -> game.getAvailableBlockBuilds(worker2).equals(
                Arrays.asList(
                        getCell(game, 2, 2),
                        getCell(game, 2, 3),
                        getCell(game, 2, 4),
                        getCell(game, 3, 2),
                        getCell(game, 3, 4),
                        getCell(game, 4, 2),
                        getCell(game, 4, 3),
                        getCell(game, 4, 4)
                )
        ));

        Worker worker4 = new Worker(getCell(game, 4,4));
        game.spawnWorker(worker4);
        getCell(game, 2, 2).setDoomed(true);

        assertTrue(() -> game.getAvailableMoves(worker2).equals(
                Arrays.asList(
                        getCell(game, 2, 3),
                        getCell(game, 2, 4),
                        getCell(game, 3, 2),
                        getCell(game, 3, 4),
                        getCell(game, 4, 2),
                        getCell(game, 4, 3)
                )
        ));
    }

    /**
     * Simulate a simple game where two turns one player loses
     */
    @Test
    void SimpleGameWithStates(){
        List<Player> players = new ArrayList<>();
        Player player1 = new Player("A", 1);
        Player player2 = new Player("B", 2);
        players.add(player1);
        players.add(player2);
        Game game1 = new Game(players, true);
        Worker worker1 = new Worker(getCell(game1,1,0));
        Worker worker2 = new Worker(getCell(game1,3,3));
        Worker worker3 = new Worker(getCell(game1,4,2));
        Worker worker4 = new Worker(getCell(game1,4,2));
        Worker worker5 = new Worker(getCell(game1,2,1));
        Worker worker6 = new Worker(getCell(game1,4,0));

        game1.spawnWorker(worker1);
        game1.spawnWorker(worker2);
        game1.spawnWorker(worker3);
        assertThrows(IllegalArgumentException.class, ()->game1.spawnWorker(worker4));
        game1.spawnWorker(worker5);

        game1.moveWorker(worker1, getCell(game1,2,0));
        assertThrows(IllegalStateException.class, game1::endTurn);
        assertThrows(IllegalArgumentException.class, ()->game1.buildDome(worker1,getCell(game1,3,0)));
        game1.buildBlock(worker1,getCell(game1,3,0));
        game1.endTurn();

        assertThrows(IllegalArgumentException.class, ()->game1.moveWorker(worker5,getCell(game1,2,0)));
        game1.moveWorker(worker3, getCell(game1,4,3));
        assertThrows(IllegalStateException.class, game1::endTurn);
        game1.buildBlock(worker3, getCell(game1,3,2));
        game1.endTurn();

        getCell(game1, 1,0).setLevel(3);
        getCell(game1, 1,1).setLevel(3);
        getCell(game1, 2,1).setLevel(3);
        getCell(game1, 2,2).setLevel(3);
        getCell(game1, 3,0).setLevel(3);
        getCell(game1, 2,3).setLevel(3);
        getCell(game1, 2,4).setLevel(3);
        getCell(game1, 3,4).setLevel(3);
        getCell(game1, 4,4).setLevel(3);
        getCell(game1, 4,3).setLevel(3);
        getCell(game1, 4,2).setLevel(3);
        getCell(game1, 3,2).setLevel(3);
        getCell(game1, 3,1).setLevel(3);
        game1.endTurn();
        assertFalse(game1.getPlayers().contains(player1));




    }

    private Cell getCell(Game game, int x, int y) {
        return game.getBoard().getCellFromCoords(x, y);
    }

    private Game constructNormalGame() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("A", 1));
        players.add(new Player("B", 2));
        players.add(new Player("C", 3));

        return new Game(players, false);
    }

}