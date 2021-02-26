package it.polimi.ingsw.client.cli;

/**
 * Util constants for cli rendering
 */
public class CliConstants {

    private static final String COLOR_PREFIX = "\u001b[";
    private static final String COLOR_SUFFIX = "m";
    private static final String BRIGHT_CODE = ";1";

    /**
     * Color constants
     */
    public enum CliColor {

        BLACK("30", "40", "Black"),
        RED("31", "41", "Red"),
        GREEN("32", "42", "Green"),
        YELLOW("33", "43", "Yellow"),
        BLUE("34", "44", "Blue"),
        MAGENTA("35", "45", "Magenta"),
        CYAN("36", "46", "Cyan"),
        WHITE("37", "47", "White");

        private final String foreground;
        private final String background;
        private final String name;

        CliColor(String foreground, String background, String name) {
            this.foreground = foreground;
            this.background = background;
            this.name = name;
        }

        public String getForeground() {
            return COLOR_PREFIX + foreground + COLOR_SUFFIX;
        }

        public String getBackground() {
            return COLOR_PREFIX + background + COLOR_SUFFIX;
        }

        public String getBrightBackground() {
            return COLOR_PREFIX + background + BRIGHT_CODE + COLOR_SUFFIX;
        }

        public String getName() {
            return name;
        }

    }

    public static final int TERM_HEIGHT = 20;

    public static final int TERM_WIDTH = 120;

    public static final String CLEAR = System.lineSeparator().repeat(TERM_HEIGHT * 10);

    public static final int HEADER_SPACING = 1;

    public static final int STATUS_SPACING = 2;

    public static final CliColor[] PLAYER_COLORS = {CliColor.BLUE, CliColor.RED, CliColor.GREEN, CliColor.MAGENTA};

    public static final String RESET = COLOR_PREFIX + "0" + COLOR_SUFFIX;

}
