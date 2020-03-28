package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Board board;

    @BeforeEach
    void setUp() {
        game = new Game();
        board = new Board();
    }


    @Test
    void checkSimpleGame() {}

    @Test
    void checkGetOpponents() {
        assertEquals(game.getOpponents(),
                     game.getPlayers().remove(game.getCurrentPlayer()));
    }

    @Test
    void checkGetAvailableGods() {List<God> availableGods = game.getAvailableGods();
        game.chooseGod(game.getCurrentPlayer(), availableGods.get(1));

        assertEquals(availableGods.remove(1), game.getAvailableGods());
    }

    @Test
    void checkGetAvailableMoves() {
        Worker worker1 = new Worker(getCell(0,0));
        Worker worker2 = new Worker(getCell(2,0));
        Worker worker3 = new Worker(getCell(3,3));
        List<Worker> workers = new ArrayList<>();
        workers.add(worker1);
        workers.add(worker2);
        workers.add(worker3);
        game.spawnWorkers(workers);

        assertTrue(() -> game.getAvailableMoves(worker1).equals(
                Arrays.asList(
                        getCell(1, 0),
                        getCell(1, 1),
                        getCell(0, 1)
                )
        ));

        assertTrue(() -> game.getAvailableMoves(worker2).equals(
                Arrays.asList(
                        getCell(1, 0),
                        getCell(1, 1),
                        getCell(2, 1),
                        getCell(3, 1),
                        getCell(3, 0)
                )
        ));

        assertTrue(() -> game.getAvailableMoves(worker2).equals(
                Arrays.asList(
                        getCell(2, 2),
                        getCell(2, 3),
                        getCell(2, 4),
                        getCell(3, 2),
                        getCell(3, 4),
                        getCell(4, 2),
                        getCell(4, 3),
                        getCell(4, 4),
                )
        ));

        Worker worker4 = new Worker(getCell(4,4));
        List<Worker> workers2 = new ArrayList<>();
        workers.add(worker4);
        game.buildBlock(worker4, getCell(4, 3));
        game.buildBlock(worker4, getCell(4, 3));

        assertTrue(() -> game.getAvailableMoves(worker2).equals(
                Arrays.asList(
                        getCell(2, 2),
                        getCell(2, 3),
                        getCell(2, 4),
                        getCell(3, 2),
                        getCell(4, 2),
                        getCell(3, 4)
                        )
        ));
    }

    @Test
    void checkGetAvailableBlockBuilds() {
        Worker worker1 = new Worker(getCell(0,0));
        Worker worker2 = new Worker(getCell(2,0));
        Worker worker3 = new Worker(getCell(3,3));
        List<Worker> workers = new ArrayList<>();
        workers.add(worker1);
        workers.add(worker2);
        workers.add(worker3);
        game.spawnWorkers(workers);

        assertTrue(() -> game.getAvailableBlockBuilds(worker1).equals(
                Arrays.asList(
                        getCell(1, 0),
                        getCell(1, 1),
                        getCell(0, 1)
                )
        ));

        assertTrue(() -> game.getAvailableBlockBuilds(worker2).equals(
                Arrays.asList(
                        getCell(1, 0),
                        getCell(1, 1),
                        getCell(2, 1),
                        getCell(3, 1),
                        getCell(3, 0)
                )
        ));

        assertTrue(() -> game.getAvailableBlockBuilds(worker2).equals(
                Arrays.asList(
                        getCell(2, 2),
                        getCell(2, 3),
                        getCell(2, 4),
                        getCell(3, 2),
                        getCell(3, 4),
                        getCell(4, 2),
                        getCell(4, 3),
                        getCell(4, 4),
                        )
        ));

        Worker worker4 = new Worker(getCell(4,4));
        List<Worker> workers2 = new ArrayList<>();
        workers.add(worker4);

        assertTrue(() -> game.getAvailableMoves(worker2).equals(
                Arrays.asList(
                        getCell(2, 2),
                        getCell(2, 3),
                        getCell(2, 4),
                        getCell(3, 2),
                        getCell(4, 2),
                        getCell(3, 4),
                        getCell(4, 3)
                )
        ));
    }

    private Cell getCell(int x, int y) {
        return board.getCellFromCoords(x, y);
    }
}