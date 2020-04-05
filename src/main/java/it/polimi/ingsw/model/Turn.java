package it.polimi.ingsw.model;

import java.util.*;
import java.util.function.Function;
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
    private final Function<Cell, List<Cell>> getNeighbours;

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
     * The starting cell
     */
    private final Cell startingCell;

    /**
     * Instantiates a Turn
     * @param worker The Worker used during the Turn
     * @param otherWorkers The other Workers present, the boolean represents whether or not they belong to the same player
     * @param getNeighbours List of neighboring to a cell
     */
    public Turn(Worker worker, Map<Worker, Boolean> otherWorkers, Function<Cell, List<Cell>> getNeighbours) {
        this.worker = worker;
        this.otherWorkers = Map.copyOf(otherWorkers);
        this.getNeighbours = getNeighbours;
        this.startingCell = worker.getCell();
    }

    public Worker getWorker() {
        return worker;
    }

    public List<Worker> getOtherWorkers() { return List.copyOf(otherWorkers.keySet()); }

    public List<Cell> getNeighbours(Cell cell) { return getNeighbours.apply(cell); }

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

    public Cell getStartingCell() { return startingCell; }

}
