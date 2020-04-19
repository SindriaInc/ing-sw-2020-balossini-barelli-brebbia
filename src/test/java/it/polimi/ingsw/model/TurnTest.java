package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TurnTest {

    private Turn turn;
    private Cell cell;

    @BeforeEach
    void setUp() {
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(0, 1);
        Cell cell3 = new Cell(3, 3);
        cell = new Cell(1, 0);
        Worker worker1 = new Worker(cell1);
        Worker worker2 = new Worker(cell2);
        Worker worker3 = new Worker(cell3);

        Map<Worker, Boolean> otherWorkers = new HashMap<>();
        otherWorkers.put(worker2, false);
        otherWorkers.put(worker3, true);

        turn = new Turn(worker1, otherWorkers, (cell) -> Arrays.asList(cell, cell2), cell -> false);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(turn.getMoves(), new ArrayList<>());
        assertEquals(turn.getBlocksPlaced(), new ArrayList<>());
        assertEquals(turn.getDomesPlaced(), new ArrayList<>());

        turn.addMovement(cell);
        turn.addBlockPlaced(cell);
        turn.addDomePlaced(cell);

        assertEquals(turn.getMoves(), Collections.singletonList(cell));
        assertEquals(turn.getBlocksPlaced(), Collections.singletonList(cell));
        assertEquals(turn.getDomesPlaced(), Collections.singletonList(cell));

        List<Worker> workers = new ArrayList<>();
        workers.add(turn.getWorker());
        for (Worker other : turn.getOtherWorkers()) {
            if (turn.hasSamePlayer(other)) {
                workers.add(other);
            }
        }

        assertEquals(turn.getCandidateWinWorkers(), workers);

        turn.addBannedWinWorker(turn.getWorker());
        workers.remove(0);

        assertEquals(turn.getCandidateWinWorkers(), workers);
    }

}