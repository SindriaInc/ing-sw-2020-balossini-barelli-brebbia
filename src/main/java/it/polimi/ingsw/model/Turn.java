package it.polimi.ingsw.model;

import java.util.*;
import java.util.function.Predicate;

public class Turn {

    /**
     * The worker used in this turn
     */
    private final Worker worker;

    /**
     * The other workers on the board in this turn
     */
    private final Map<Worker, Boolean> otherWorkers;

    /**
     * The neighbouring cells
     */
    private final Predicate<Cell> isNeighbour;

    /**
     * List of cell occupied by the worker
     */
    private final List<Cell> moves = new ArrayList<>();

    /**
     * List of blocks placed by the worker
     */
    private final List<Cell> blocksPlaced = new ArrayList<>();

    /**
     * List of domes placed by the worker
     */
    private final List<Cell> domesPlaced = new ArrayList<>();

    /**
     * Instantiates a Turn
     * @param worker The Worker used during the Turn
     * @param otherWorkers The other Workers present, the boolean represents whether or not they belong to the same player
     * @param isNeighbour Supplier that checks if a given cell is a neighbour of the worker position
     */
    public Turn(Worker worker, Map<Worker, Boolean> otherWorkers, Predicate<Cell> isNeighbour) {
        this.worker = worker;
        this.otherWorkers = Map.copyOf(otherWorkers);
        this.isNeighbour = isNeighbour;
    }

    public Worker getWorker() {
        return worker;
    }

    public List<Worker> getOtherWorkers() {
        return List.copyOf(otherWorkers.keySet());
    }

    public boolean isNeighbour(Cell cell) {
        return isNeighbour.test(cell);
    }

    public boolean hasSamePlayer(Worker worker) {
        return otherWorkers.get(worker);
    }

    public List<Cell> getMoves() {
        return List.copyOf(moves);
    }

    public List<Cell> getBlocksPlaced() {
        return List.copyOf(blocksPlaced);
    }

    public List<Cell> getDomesPlaced() {
        return List.copyOf(domesPlaced);
    }

    public void addMovement(Cell cell) {
        moves.add(cell);
    }

    public void addBlockPlaced(Cell cell) {
        blocksPlaced.add(cell);
    }

    public void addDomePlaced(Cell cell) {
        domesPlaced.add(cell);
    }

}
