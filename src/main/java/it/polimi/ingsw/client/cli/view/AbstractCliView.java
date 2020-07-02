package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;

import java.util.List;

/**
 * Generates the cli views
 */
public abstract class AbstractCliView {

    private final int lineLength;

    /**
     * Class constructor, set the line length to be used in the rendering
     *
     * @param lineLength The line length
     */
    public AbstractCliView(int lineLength) {
        this.lineLength = lineLength;
    }

    /**
     * Builds the view
     * @return the output of the view as a printable String
     */
    public abstract String generateView();

    /**
     * Builds the commands available to the user, to be interpreted by the viewer
     * @return the list of cli commands
     */
    public abstract List<CliCommand> generateCommands();

    /**
     * Build the header
     * @param header The header as a string array
     * @return The header as a string
     */
    protected String buildHeader(String[] header) {
        StringBuilder output = new StringBuilder();

        int largestLineLength = largestLine(header);

        for (String line : header) {
            output.append(center(line, largestLineLength)).append(System.lineSeparator());
        }

        return output.toString();
    }

    /**
     * Print a centered text
     * @param toPrint The text
     * @return The text with additional spaces
     */
    protected String center(String toPrint) {
        return center(toPrint, toPrint.length());
    }

    /**
     * Print a centered text given a limit on the right
     * @param toPrint The text
     * @param maxLength The limit offset
     * @return The text with additional spaces
     */
    protected String center(String toPrint, int maxLength) {
        int startOS = (lineLength - maxLength) / 2;
        return " ".repeat(startOS) + toPrint;
    }

    /**
     * Create a separator
     * @return The separator's string
     */
    protected String separator() {
        return "=".repeat(lineLength) + System.lineSeparator();
    }

    /**
     * Print a centered text given a padding on the left
     * @param toPrint The text
     * @param padding The padding
     * @return The text with additional spaces
     */
    protected String leftPadAndCenter(String toPrint, int padding) {
        int startOS = (lineLength - padding - toPrint.length()) / 2;
        return " ".repeat(startOS) + toPrint;
    }

    /**
     * Get the length of the longest string in a string array
     * @param strings The string array
     * @return The longest line's length
     */
    protected int largestLine(String[] strings) {
        int largestLineLength = 0;

        for (String line : strings) {
            if (line.length() > largestLineLength) {
                largestLineLength = line.length();
            }
        }

        return largestLineLength;
    }

}
