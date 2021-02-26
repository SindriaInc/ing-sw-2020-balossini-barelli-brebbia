package it.polimi.ingsw.model;

import java.util.List;

public class TestConstants {

    public static final int BOARD_TEST_ROWS = 5;
    public static final int BOARD_TEST_COLUMNS = 5;

    public static final String PLAYER_NAME = "gInton1c";
    public static final int PLAYER_AGE = 42;

    public static final String GOD_NAME = "A";
    public static final int GOD_ID = 1;
    public static final String GOD_DESCRIPTION = "Standard";
    public static final String GOD_TYPE = "Letter";

    public static final int MAX_WORKERS = 2;

    private TestConstants() {}

    public static <T> boolean equalsNoOrder(List<T> list1, List<T> list2) {
        return (list1.size() == list2.size()) &&
                list1.containsAll(list2) &&
                list2.containsAll(list1);
    }

}
