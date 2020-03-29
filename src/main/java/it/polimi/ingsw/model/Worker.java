package it.polimi.ingsw.model;

public class Worker {

    /**
     * The worker's current position
     */
    private Cell cell;

    /**
     * The worker's previous position
     */
    private Cell previousCell;

    public Worker(Cell cell) {
        this.cell = cell;
        this.previousCell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    /**
     * Checks if the player has gone up at least 1 level in his most recent turn
     * @return true if the current cell is at least 1 level higher than the previous one
     */
    // TODO: Implement method
    public boolean hasMovedUp() { return false; }

    /**
     * Move the player to the cell, updating the player current and previous position
     * @param cell The Cell
     */
    // TODO: Implement method
    public void move(Cell cell) { }

}
