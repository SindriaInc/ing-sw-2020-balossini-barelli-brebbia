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

    /**
     * True if previousCell was the worker position before being forced on the current cell
     */
    private boolean previousForced;

    public Worker(Cell cell) {
        this.cell = cell;
        this.previousCell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public Cell getPreviousCell() {return previousCell;}

    /**
     * Checks if the worker has gone up at least 1 level in his most recent turn
     * @return true if the current cell is at least 1 level higher than the previous one
     */
    public boolean hasMovedUp() {
        return !previousForced && cell.getLevel() > previousCell.getLevel();
    }

    /**
     * Move the worker to the cell, updating the player current and previous position
     * @param cell The Cell
     */
    public void move(Cell cell) {
        this.previousCell = this.cell;
        this.cell = cell;
        this.previousForced = false;
    }

    /**
     * Force the worker to the cell, updating the player current and previous position
     * @param cell The Cell
     */
    public void force(Cell cell) {
        move(cell);
        this.previousForced = true;
    }

}
