package it.polimi.ingsw.model;

import java.util.Optional;

public class Worker {

    private static class Movement {

        private final int previousLevel;
        private Cell next;
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
     * Move the worker to the cell, updating the player current and previous position
     * @param cell The Cell
     */
    public void move(Cell cell) {
        lastMovement = new Movement(this.cell, this.cell.getLevel(), cell, cell.getLevel());
        force(cell);
    }

    /**
     * Force the worker to the cell, updating the player current and previous position
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
