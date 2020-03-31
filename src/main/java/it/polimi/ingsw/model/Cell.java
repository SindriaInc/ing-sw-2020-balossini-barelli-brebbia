package it.polimi.ingsw.model;

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

    protected Cell(int x, int y) {
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

}
