package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void checkInvalidCoords() {
        assertThrows(IllegalArgumentException.class, () -> getCell(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> getCell(0, -1));
        assertThrows(IllegalArgumentException.class, () -> getCell(-1, -1));
        assertThrows(IllegalArgumentException.class, () -> getCell(Board.COLUMNS, 0));
        assertThrows(IllegalArgumentException.class, () -> getCell(0, Board.ROWS));
        assertThrows(IllegalArgumentException.class, () -> getCell(Board.COLUMNS, Board.ROWS));
    }

    @Test
    void checkCorrectCell() {
        assertTrue(() -> {
            Cell cell = getCell(0, 0);
            return cell.getX() == 0 && cell.getY() == 0;
        });

        assertTrue(() -> {
            Cell cell = getCell(0, 1);
            return cell.getX() == 0 && cell.getY() == 1;
        });

        assertTrue(() -> {
            Cell cell = getCell(1, 0);
            return cell.getX() == 1 && cell.getY() == 0;
        });

        assertTrue(() -> {
            Cell cell = getCell(1, 1);
            return cell.getX() == 1 && cell.getY() == 1;
        });
    }

    @Test
    void checkPerimeterSpace() {
        assertTrue(() -> board.isPerimeterSpace(getCell(0, 0)));
        assertTrue(() -> board.isPerimeterSpace(getCell(0, Board.ROWS - 1)));
        assertTrue(() -> board.isPerimeterSpace(getCell(Board.COLUMNS - 1, 0)));
        assertTrue(() -> board.isPerimeterSpace(getCell(Board.COLUMNS - 1, Board.ROWS - 1)));
        assertTrue(() -> board.isPerimeterSpace(getCell(1, 0)));
        assertTrue(() -> board.isPerimeterSpace(getCell(0, 1)));
    }

    @Test
    void checkNotPerimeterSpace() {
        assertFalse(() -> board.isPerimeterSpace(getCell(1, 1)));
    }

    @Test
    void checkNeighborings() {
        assertTrue(() -> board.getNeighborings(getCell(0, 0)).equals(
                Arrays.asList(
                        getCell(1, 0),
                        getCell(1, 1),
                        getCell(0, 1)
                )
        ));

        assertTrue(() -> board.getNeighborings(getCell(1, 0)).equals(
                Arrays.asList(
                        getCell(0, 0),
                        getCell(0, 1),
                        getCell(1, 1),
                        getCell(2, 1),
                        getCell(2, 0)
                )
        ));

        assertTrue(() -> board.getNeighborings(getCell(1, 1)).equals(
                Arrays.asList(
                        getCell(0, 0),
                        getCell(0, 1),
                        getCell(0, 2),
                        getCell(1, 2),
                        getCell(2, 2),
                        getCell(2, 1),
                        getCell(2, 0),
                        getCell(1, 0)
                )
        ));
    }

    private Cell getCell(int x, int y) {
        return board.getCellFromCoords(x, y);
    }

}