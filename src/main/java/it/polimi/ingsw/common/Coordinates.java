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

    @Override
    public String toString() {
        return x + "," + y;
    }

    public static Coordinates parse(String string) {
        String[] raw = string.split(",");
        int x = Integer.parseInt(raw[0]);
        int y = Integer.parseInt(raw[1]);
        return new Coordinates(x, y);
    }

}
