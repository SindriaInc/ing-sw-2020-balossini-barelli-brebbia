package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;

import java.util.List;

public abstract class AbstractCliView {

    private final int lineLength;

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

    protected String buildHeader(String[] header) {
        StringBuilder output = new StringBuilder();

        int largestLineLength = largestLine(header);

        for (String line : header) {
            output.append(center(line, largestLineLength)).append(System.lineSeparator());
        }

        return output.toString();
    }

    protected String center(String toPrint) {
        return center(toPrint, toPrint.length());
    }

    protected String center(String toPrint, int maxLength) {
        int startOS = (lineLength - maxLength) / 2;
        return " ".repeat(startOS) + toPrint;
    }

    protected String separator() {
        return "=".repeat(lineLength) + System.lineSeparator();
    }

    protected String slimSeparator() {
        return "-".repeat(lineLength) + System.lineSeparator();
    }

    protected String leftPadAndCenter(String toPrint, int padding) {
        int startOS = (lineLength - padding - toPrint.length()) / 2;
        return " ".repeat(startOS) + toPrint;
    }

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
