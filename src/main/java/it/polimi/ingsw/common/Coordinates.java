package it.polimi.ingsw.common;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Coordinates other = (Coordinates) object;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public static Coordinates parse(String string) {
        String[] raw = string.split(",");
        int x = Integer.parseInt(raw[0]);
        int y = Integer.parseInt(raw[1]);
        return new Coordinates(x, y);
    }

}
