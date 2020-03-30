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
        for (int x = 0; x < COLUMNS; x++) {
            for (int y = 0; y < ROWS; y++) {
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

    /**
     * Checks if the cell is at the border of the board
     * @param cell The Cell
     * @return true if the cell doesn't have a neighbor in every possible direction
     */
    // TODO: Implement method
    public boolean isPerimeterSpace(Cell cell) { return false; }

    /**
     * Obtain a list of adiacent cells to the given cell
     * @param cell The Cell
     * @return The List
     */
    // TODO: Implement method
    public List<Cell> getNeighborings(Cell cell) { return null; }

}
