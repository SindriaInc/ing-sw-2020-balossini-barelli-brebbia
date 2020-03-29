package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.abilities.decorators.BlockOnPlayerMoveUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultAbilitiesTest {

    private Board board;
    private Player player;
    private DefaultAbilities abilities;

    @BeforeEach
    void setUp() {
        board = new Board();
        player = new Player(Player.DEFAULT_NAME, Player.DEFAULT_AGE);
        abilities = new DefaultAbilities(board, player);
    }

    @Test
    void checkWinCondition() {
        getCell(0, 0).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL - 1);
        getCell(0, 1).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL);

        Worker worker = new Worker(getCell(0, 0));
        player.addWorker(worker);

        assertFalse(abilities.checkHasWon());

        abilities.doMove(worker, getCell(0, 1));
        assertTrue(abilities.checkHasWon());
    }

    @Test
    void checkNoUpNoWin() {
        getCell(0, 0).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL);
        getCell(0, 1).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL);

        Worker worker = new Worker(getCell(0, 0));
        player.addWorker(worker);

        assertFalse(abilities.checkHasWon());

        abilities.doMove(worker, getCell(0, 1));
        assertFalse(abilities.checkHasWon());
    }

    @Test
    void checkNoMovementNoWin() {
        getCell(0, 0).setLevel(DefaultAbilities.DEFAULT_WIN_LEVEL);
        Worker worker = new Worker(getCell(0, 0));
        player.addWorker(worker);

        assertFalse(abilities.checkHasWon());
    }

    @Test
    void checkCanMove() {
        getCell(0, 1).setLevel(1);
        getCell(1, 1).setLevel(2);
        Worker worker = new Worker(getCell(0, 0));
        player.addWorker(worker);

        assertTrue(abilities.checkCanMove(worker, getCell(0, 1)));
        assertTrue(abilities.checkCanMove(worker, getCell(1, 0)));
        assertFalse(abilities.checkCanMove(worker, getCell(1, 1)));

        abilities.doMove(worker, getCell(0, 1));

        assertTrue(abilities.checkCanMove(worker, getCell(0, 1)));
        assertTrue(abilities.checkCanMove(worker, getCell(1, 0)));
        assertTrue(abilities.checkCanMove(worker, getCell(1, 1)));
        assertTrue(abilities.checkCanMove(worker, getCell(0, 2)));
        assertTrue(abilities.checkCanMove(worker, getCell(1, 2)));

        abilities.doMove(worker, getCell(1, 1));

        assertTrue(abilities.checkCanMove(worker, getCell(0, 0)));
        assertTrue(abilities.checkCanMove(worker, getCell(0, 1)));
        assertTrue(abilities.checkCanMove(worker, getCell(0, 2)));
        assertTrue(abilities.checkCanMove(worker, getCell(1, 2)));
        assertTrue(abilities.checkCanMove(worker, getCell(2, 2)));
        assertTrue(abilities.checkCanMove(worker, getCell(2, 1)));
        assertTrue(abilities.checkCanMove(worker, getCell(2, 0)));
        assertTrue(abilities.checkCanMove(worker, getCell(1, 0)));
    }

    @Test
    void doMove() {
        getCell(0, 1).setLevel(1);

        Worker worker = new Worker(getCell(0, 0));
        player.addWorker(worker);
        abilities.doMove(worker, getCell(0, 1));

        assertEquals(worker.getCell(), getCell(0, 1));
    }

    @Test
    void checkCanBuildBlock() {
        getCell(0, 1).setLevel(1);
        getCell(1, 1).setLevel(2);
        Worker worker = new Worker(getCell(0, 0));
        player.addWorker(worker);

        assertTrue(abilities.checkCanBuildBlock(worker, getCell(0, 1)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(1, 0)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(1, 1)));

        abilities.doMove(worker, getCell(0, 1));

        assertTrue(abilities.checkCanBuildBlock(worker, getCell(0, 1)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(1, 0)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(1, 1)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(0, 2)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(1, 2)));

        abilities.doMove(worker, getCell(1, 1));

        assertTrue(abilities.checkCanBuildBlock(worker, getCell(0, 0)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(0, 1)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(0, 2)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(1, 2)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(2, 2)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(2, 1)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(2, 0)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(1, 0)));
    }

    @Test
    void doBuildBlock() {
        Worker worker = new Worker(getCell(0, 0));
        player.addWorker(worker);
        abilities.doBuildBlock(worker, getCell(0, 1));

        assertEquals(getCell(0, 1).getLevel(), 1);
    }

    @Test
    void checkCanBuildDome() {
        getCell(0, 1).setLevel(1);
        getCell(1, 1).setLevel(2);
        getCell(2, 1).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        Worker worker = new Worker(getCell(0, 0));
        player.addWorker(worker);

        assertFalse(abilities.checkCanBuildBlock(worker, getCell(0, 1)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(1, 0)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(1, 1)));

        abilities.doMove(worker, getCell(0, 1));

        assertFalse(abilities.checkCanBuildBlock(worker, getCell(0, 1)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(1, 0)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(1, 1)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(0, 2)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(1, 2)));

        abilities.doMove(worker, getCell(1, 1));

        assertFalse(abilities.checkCanBuildBlock(worker, getCell(0, 0)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(0, 1)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(0, 2)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(1, 2)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(2, 2)));
        assertTrue(abilities.checkCanBuildBlock(worker, getCell(2, 1)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(2, 0)));
        assertFalse(abilities.checkCanBuildBlock(worker, getCell(1, 0)));
    }

    @Test
    void doBuildDome() {
        getCell(0, 1).setLevel(DefaultAbilities.DEFAULT_DOME_LEVEL);
        Worker worker = new Worker(getCell(0, 0));
        player.addWorker(worker);
        abilities.doBuildDome(worker, getCell(0, 1));

        assertTrue(getCell(0, 1).isDoomed());
    }

    private Cell getCell(int x, int y) {
        return board.getCellFromCoords(x, y);
    }

}