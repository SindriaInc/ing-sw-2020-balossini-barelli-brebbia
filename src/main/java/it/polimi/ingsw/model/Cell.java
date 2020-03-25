package it.polimi.ingsw.model;

public class Cell {

    /**
     * The column number
     */
    final int x;

    /**
     * The row number
     */
    final int y;

    /**
     * Current building height, default Board.BASE_LEVEL
     */
    int level;

    /**
     * Whether or not there's a dome on the cell, default FALSE
     */
    boolean doomed;

    protected Cell(int x, int y) {
        this.x = x;
        this.y = y;

        this.level = Board.BASE_LEVEL;
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
