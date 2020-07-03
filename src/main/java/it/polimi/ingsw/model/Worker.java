package it.polimi.ingsw.model;

import java.util.Optional;

/**
 * The class representing a worker, the unit that moves in the board and can build blocks/domes
 * Each player has a set number of workers
 */
public class Worker {

    /**
     * The class representing the movement of the worker,
     * with cell where the worker was in the last turn and
     * the level of that cell
     */
    private static class Movement {

        private final int previousLevel;
        private final Cell next;
        private final int nextLevel;

        Movement(Cell previous, int previousLevel, Cell next, int nextLevel) {
            this.previousLevel = previousLevel;
            this.next = next;
            this.nextLevel = nextLevel;
        }

    }

    /**
     * The worker's id
     */
    private final int id;

    /**
     * The worker's current position
     */
    private Cell cell;

    /**
     * The worker's last movement
     * Can be null if there was no last movement
     */
    private Movement lastMovement;

    /**
     * Class constructor
     * @param id The work id
     * @param cell The cell where the worker is present
     */
    public Worker(int id, Cell cell) {
        this.id = id;
        this.cell = cell;
    }

    public int getId() {
        return id;
    }

    public Cell getCell() {
        return cell;
    }

    public Optional<Cell> getLastMovementCell() {
        if (lastMovement == null) {
            return Optional.empty();
        }

        return Optional.of(lastMovement.next);
    }

    public Optional<Integer> getLastMovementLevelDifference() {
        if (lastMovement == null) {
            return Optional.empty();
        }

        return Optional.of(lastMovement.nextLevel - lastMovement.previousLevel);
    }

    /**
     * Moves the worker to the cell, updating the player current and previous position
     * @param cell The Cell
     */
    public void move(Cell cell) {
        lastMovement = new Movement(this.cell, this.cell.getLevel(), cell, cell.getLevel());
        force(cell);
    }

    /**
     * Forces the worker to the cell, updating the player current and previous position
     * @param cell The Cell
     */
    public void force(Cell cell) {
        this.cell = cell;
    }

    /**
     * Removes the last movement
     */
    public void clearMovement() {
        lastMovement = null;
    }

}
