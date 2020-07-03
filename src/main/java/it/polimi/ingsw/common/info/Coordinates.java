package it.polimi.ingsw.common.info;

import java.util.Objects;

/**
 * An object representing a pair of x and y variables
 */
public class Coordinates {

    /**
     * The column
     */
    private final int x;

    /**
     * The row
     */
    private final int y;

    /**
     * Class constructor
     *
     * @param x The column
     * @param y The row
     */
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
        return x == other.x &&
                y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
