package it.polimi.ingsw.model;

import it.polimi.ingsw.common.info.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

    /**
     * The number of rows (y coordinate)
     */
    private final int rows;

    /**
     * The number of columns (x coordinate)
     */
    private final int columns;

    /**
     * 2-dimensional array containing each cell in the board
     */
    private final Cell[][] map;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.map = new Cell[rows][columns];

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                map[x][y] = new Cell(x, y);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    /**
     * Obtain Cell object from given coordinates
     * @param coordinates The coordinates
     * @return The Cell
     * @throws IllegalArgumentException If x or y are out of bounds
     */
    public Cell getCell(Coordinates coordinates) throws IllegalArgumentException {
        return getCellFromCoords(coordinates.getX(), coordinates.getY());
    }

    /**
     * Obtain Cell object from given coordinates
     * @param x The row, must be between 0 and COLUMNS - 1
     * @param y The column, must be between 0 and ROWS - 1
     * @return The Cell
     * @throws IllegalArgumentException If x or y are out of bounds
     */
    public Cell getCellFromCoords(int x, int y) throws IllegalArgumentException {
        if (x < 0 || x >= getColumns() || y < 0 || y >= getRows()) {
            throw new IllegalArgumentException("Invalid coordinates");
        }

        return map[x][y];
    }

    /**
     * Checks if the cell is at the border of the board
     * @param cell The Cell
     * @return true if the cell doesn't have a neighbor in every possible direction
     */
    public boolean isPerimeterSpace(Cell cell) {
        int x = cell.getX();
        int y = cell.getY();

        return x == 0 || y == 0 || x == getColumns() - 1 || y == getRows() - 1;
    }

    /**
     * Obtain a list of adiacent cells to the given cell
     * @param cell The Cell
     * @return The List
     */
    public List<Cell> getNeighborings(Cell cell) {
        List<Cell> neighborings = new ArrayList<>();
        int x = cell.getX();
        int y = cell.getY();

        // Ends of the square with center in the given cell
        int minX = x - 1;
        int maxX = x + 1;
        int minY = y - 1;
        int maxY = y + 1;

        //Reduce the size of the square doing an intersection with the board
        if (x == 0) {
            minX = 0;
        } else if (x == getColumns() - 1) {
            maxX = getColumns() - 1;
        }
        if (y == 0) {
            minY = 0;
        } else if (y == getRows() - 1) {
            maxY = getRows() - 1;
        }

        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                neighborings.add(getCellFromCoords(i, j));
            }
        }

        // Remove the given cell
        neighborings.remove(cell);
        return neighborings;
    }

    /**
     * Obtain a list of every cell, in no particular order
     */
    public List<Cell> getCells() {
        return Arrays.stream(map).flatMap(Arrays::stream).collect(Collectors.toList());
    }

}
