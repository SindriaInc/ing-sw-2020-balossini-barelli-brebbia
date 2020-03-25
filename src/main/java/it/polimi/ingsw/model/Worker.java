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

    // TODO: Implement methods
    public boolean hasMovedUp() { return false; }
    public void move(Cell cell) { }

}
