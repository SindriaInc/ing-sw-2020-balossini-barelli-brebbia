package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(TestConstants.BOARD_TEST_ROWS, TestConstants.BOARD_TEST_COLUMNS);
    }

    @Test
    void checkInvalidCoords() {
        assertThrows(IllegalArgumentException.class, () -> getCell(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> getCell(0, -1));
        assertThrows(IllegalArgumentException.class, () -> getCell(-1, -1));
        assertThrows(IllegalArgumentException.class, () -> getCell(TestConstants.BOARD_TEST_COLUMNS, 0));
        assertThrows(IllegalArgumentException.class, () -> getCell(0, TestConstants.BOARD_TEST_ROWS));
        assertThrows(IllegalArgumentException.class, () -> getCell(TestConstants.BOARD_TEST_COLUMNS, TestConstants.BOARD_TEST_ROWS));
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
        assertTrue(() -> board.isPerimeterSpace(getCell(0, TestConstants.BOARD_TEST_ROWS - 1)));
        assertTrue(() -> board.isPerimeterSpace(getCell(TestConstants.BOARD_TEST_COLUMNS - 1, 0)));
        assertTrue(() -> board.isPerimeterSpace(getCell(TestConstants.BOARD_TEST_COLUMNS - 1, TestConstants.BOARD_TEST_ROWS - 1)));
        assertTrue(() -> board.isPerimeterSpace(getCell(1, 0)));
        assertTrue(() -> board.isPerimeterSpace(getCell(0, 1)));
    }

    @Test
    void checkNotPerimeterSpace() {
        assertFalse(() -> board.isPerimeterSpace(getCell(1, 1)));
    }

    @Test
    void checkNeighborings() {
        assertTrue(() -> equalsNoOrder(board.getNeighborings(getCell(0, 0)),
                Arrays.asList(
                        getCell(0, 1),
                        getCell(1, 0),
                        getCell(1, 1)
                )
        ));

        assertTrue(() -> equalsNoOrder(board.getNeighborings(getCell(1, 0)),
                Arrays.asList(
                        getCell(0, 0),
                        getCell(0, 1),
                        getCell(1, 1),
                        getCell(2, 1),
                        getCell(2, 0)
                )
        ));

        assertTrue(() -> equalsNoOrder(board.getNeighborings(getCell(1, 1)),
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

    private boolean equalsNoOrder(List<Cell> list1, List<Cell> list2) {
        return (list1.size() == list2.size()) &&
                list1.containsAll(list2) &&
                list2.containsAll(list1);
    }

}