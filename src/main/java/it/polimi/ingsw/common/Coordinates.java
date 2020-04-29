package it.polimi.ingsw.common;

public class Coordinates {

    /**
     * The row
     */
    private final int x;

    /**
     * The column
     */
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
