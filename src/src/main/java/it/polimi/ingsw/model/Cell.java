package it.polimi.ingsw.model;

import java.util.Objects;

/**
 * The class representing the cell fo the board. In the cell a worker con move, build a block or a dome and force other
 * players in.
 * Each cell can contain only one worker and is dived in three levels of altitude where, at the top, a player can climb
 * to win or place a dome
 */
public class Cell {

    /**
     * The column number
     */
    private final int x;

    /**
     * The row number
     */
    private final int y;

    /**
     * Current building height, default 0
     */
    private int level;

    /**
     * Whether or not there's a dome on the cell, default FALSE
     */
    private boolean doomed;

    /**
     * Class constructor
     * @param x The cell's column
     * @param y The cell's row
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isDoomed() {
        return doomed;
    }

    public void setDoomed(boolean doomed) {
        this.doomed = doomed;
    }

    @Override
    public String toString() {
        return "{x: " + x + ", y: " + y + ", doomed: " + doomed + "}";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Cell cell = (Cell) object;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
