package it.polimi.ingsw.model;

import java.util.ArrayList;
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
    public boolean isPerimeterSpace(Cell cell) {
        int x=cell.getX();
        int y=cell.getY();

        if (x==0 || y==0 || x==(COLUMNS-1) || y==(ROWS-1))
            return true;
        return false;
    }

    /**
     * Obtain a list of adiacent cells to the given cell
     * @param cell The Cell
     * @return The List
     */
    public List<Cell> getNeighborings(Cell cell) {
        List<Cell> neighborings = new ArrayList<>();
        int x=cell.getX();
        int y=cell.getY();
        //Ends of the square with center in the given cell
        int minX=x-1, maxX=x+1, minY=y-1, maxY=y+1;

        //Reduce the size of the square doing an intersection with the board
        if (x==0)
            minX=0;
        else if (x==COLUMNS-1)
            maxX=COLUMNS-1;
        if (y==0)
            minY=0;
        else if (y==ROWS-1)
            maxY=ROWS-1;

        for (int i=minX; i<=maxX; i++)
        {
            for (int j=minY; j<=maxY; j++) {
                neighborings.add(getCellFromCoords(i, j));
            }
        }
        //Remove the given cell
        neighborings.remove(cell);

        return neighborings;
    }

}
