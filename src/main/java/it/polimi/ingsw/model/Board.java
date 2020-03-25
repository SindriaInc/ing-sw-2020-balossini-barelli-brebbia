package it.polimi.ingsw.model;

import java.util.List;

public class Board {

    public static final int BASE_LEVEL = 0;
    public static final int ROWS = 5;
    public static final int COLUMNS = 5;

    /**
     * 2-dimensional array containing each cell in the board
     */
    private final Cell[][] map = new Cell[ROWS][COLUMNS];

    public Board() {
        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLUMNS; y++) {
                map[x][y] = new Cell(x, y);
            }
        }
    }

    /**
     * Obtain Cell object from given coordinates
     * @param x The row, must be between 0 and COLUMNS - 1
     * @param y The column, must be between 0 and ROWS - 1
     * @return The Cell
     * @throws IllegalArgumentException If x or y are out of bounds
     */
    public Cell getCellFromCoords(int x, int y) throws IllegalArgumentException {
        if (x < 0 || x >= COLUMNS || y < 0 || y >= ROWS) {
            throw new IllegalArgumentException("Invalid coordinates");
        }

        return map[x][y];
    }

    // TODO: Implement methods
    public boolean isPerimeterSpace(Cell cell) { return false; }
    public List<Cell> getNeighborings(Cell cell) { return null; }

}
