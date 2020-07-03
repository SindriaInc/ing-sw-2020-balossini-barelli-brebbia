package it.polimi.ingsw.client.cli.view.game;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.CliConstants;
import it.polimi.ingsw.client.cli.view.AbstractCliView;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.common.info.GodInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.client.cli.CliConstants.*;

/**
 * Generate cli view of the god list and the responses to commands
 */
public class CliGodsView extends AbstractGameView {

    private static final int ADDITIONAL_SPACE_FOR_WAIT = 10;

    private static final String GODS_HEADER = "    ID  NAME         TYPE       TITLE                 DESCRIPTION";
    private static final String PADDING = "    ";
    private static final int OS_ID = 4;
    private static final int OS_NAME = 8;
    private static final int OS_TYPE = 21;
    private static final int OS_TITLE = 32;
    private static final int OS_DESCRIPTION = 54;
    private static final int OS_END = TERM_WIDTH - PADDING.length();
    private static final int AFTER_TABLE_HEADER = 2;

    private static final String SELECT_FAIL = "Please, type the name of the gods you want to select (Example: select Athena Hera Zeus)";
    private static final String CHOOSE_FAIL = "Please, type the name of the gos you want to chose (Example: choose Atlas)";
    private static final String FIRST_FAIL = "Please, type the name of the player that will start (Example: first NicePlayer1)";

    private final GameState state;

    /**
     * Class constructor, generate a gods view given the client state and the line length
     *
     * @param state The client state
     * @param lineLength The line length
     */
    public CliGodsView(GameState state, int lineLength) {
        super(state, lineLength);

        this.state = super.getState();
    }

    /**
     * @see AbstractCliView#generateView()
     */
    @Override
    public String generateView() {
        GameData data = getState().getData();

        List<GodInfo> gods;
        StringBuilder output = new StringBuilder();

        if (data.getSelectFirstData().isPresent()) {
            output.append(separator()).append(System.lineSeparator().repeat(CliConstants.HEADER_SPACING));
            output.append(center("You are the challenger!")).append(System.lineSeparator());
            output.append(center("Select the first player to spawn workers.")).append(System.lineSeparator().repeat(AFTER_TABLE_HEADER));

            for (String player : data.getSelectFirstData().get().getAvailablePlayers()) {
                output.append(center(player)).append(System.lineSeparator());
            }

            output.append(System.lineSeparator().repeat(AFTER_TABLE_HEADER));
            output.append(separator()).append(System.lineSeparator());
            return output.toString();
        }

        if (data.getSelectGodsData().isPresent()) {
            gods = data.getSelectGodsData().get().getAvailableGods();
        } else if (data.getChooseGodData().isPresent()) {
            gods = data.getChooseGodData().get().getAvailableGods();
        } else {
            output.append(separator())
                    .append(System.lineSeparator().repeat(ADDITIONAL_SPACE_FOR_WAIT))
                    .append(center("Waiting for your turn...")).append(System.lineSeparator())
                    .append(System.lineSeparator().repeat(ADDITIONAL_SPACE_FOR_WAIT))
                    .append(separator()).append(System.lineSeparator());

            return output.toString();
        }

        output.append(separator()).append(System.lineSeparator().repeat(CliConstants.HEADER_SPACING));
        output.append(GODS_HEADER).append(System.lineSeparator().repeat(AFTER_TABLE_HEADER));

        for (GodInfo god : gods) {
            boolean firstRow = true;

            String name = god.getName();
            String type = god.getType();
            String title = god.getTitle();
            List<String> decomposedTitle = new ArrayList<>();
            decomposedTitle.addAll(Arrays.asList(title.split(" ")));
            String description = god.getDescription();
            List<String> decomposedDescription = new ArrayList<>();
            decomposedDescription.addAll(Arrays.asList(description.split(" ")));

            while (decomposedTitle.size() > 0 || decomposedDescription.size() > 0) {
                //Add the padding and initiate the current offset to 4
                output.append(PADDING);
                int currOS = PADDING.length();

                if (firstRow) {
                    //Add ID. If less then 10 print on the right
                    if (god.getId() < 10) {
                        output.append(" ").append(god.getId()).append("  ");
                    } else {
                        output.append(god.getId()).append("  ");
                    }

                    //Add name
                    output.append(name);
                    output.append(" ".repeat(OS_TYPE - OS_NAME - name.length()));

                    //Add type
                    output.append(type);
                    output.append(" ".repeat(OS_TITLE - OS_TYPE - type.length()));

                    //Add first part of title
                    currOS = printUntilLimit(output, decomposedTitle, OS_TITLE, OS_DESCRIPTION - 2);

                    //Add description
                    output.append(" ".repeat(OS_DESCRIPTION - currOS));
                    currOS = printUntilLimit(output, decomposedDescription, OS_DESCRIPTION, OS_END);

                    firstRow = false;
                    output.append(System.lineSeparator());
                } else {
                    if (decomposedTitle.size() > 0) {
                        output.append(" ".repeat(OS_TITLE - currOS));
                        currOS = printUntilLimit(output, decomposedTitle, OS_TITLE, OS_DESCRIPTION - 2);
                    }

                    if (decomposedDescription.size() > 0) {
                        output.append(" ".repeat(OS_DESCRIPTION - currOS));
                        currOS = printUntilLimit(output, decomposedDescription, OS_DESCRIPTION, OS_END);
                    }
                    output.append(System.lineSeparator());
                }
            }

            output.append(System.lineSeparator());

        }

        output.append(separator()).append(System.lineSeparator());

        return output.toString();
    }

    /**
     * @see AbstractCliView#generateCommands()
     */
    @Override
    public List<CliCommand> generateCommands() {
        GameData data = getState().getData();

        if (data.getTurnPlayer().isPresent() &&
                data.getTurnPlayer().get().equals(data.getName()) &&
                !data.isSpectating()) {
            if (data.getSelectGodsData().isPresent()) {
                String[] args;
                if (data.getOtherPlayers().size() + 1 == 2) {
                    args = new String[]{"<God1>", "<God2>"};
                } else if (data.getOtherPlayers().size() + 1 == 3) {
                    args = new String[]{"<God1>", "<God2>", "<God3>"};
                } else {
                    args = new String[]{"<God1>", "<God2>", "<God3>", "<God4>"};
                }

                return List.of(
                        new CliCommand("select", args, "Select the gods to use in this game", this::onSelect)
                );
            } else if (data.getChooseGodData().isPresent()) {
                return List.of(
                        new CliCommand("choose", new String[]{"<God>"}, "Choose a god", this::onChoose)
                );
            } else if (data.getSelectFirstData().isPresent()) {
                return List.of(
                        new CliCommand("first", new String[]{"<player>"}, "Select the first player", this::onFirst)
                );
            }
        }

        return List.of();
    }

    /**
     * Respond to a gods select command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onSelect(String[] arguments) {
        GameData data = getState().getData();

        if (arguments.length != data.getOtherPlayers().size() + 1) {
            return Optional.of(SELECT_FAIL);
        }

        for (String argument : arguments) {
            if (argument.length() <= 0) {
                return Optional.of(SELECT_FAIL);
            }
        }

        List<String> selectedGods = List.of(arguments);
        state.acceptSelectGods(selectedGods);

        return Optional.empty();
    }

    /**
     * Respond to a god choose command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onChoose(String[] arguments) {
        if (arguments.length != 1) {
            return Optional.of(CHOOSE_FAIL);
        }

        if (arguments[0].length() <= 0) {
            return Optional.of(CHOOSE_FAIL);
        }

        state.acceptChooseGod(arguments[0]);

        return Optional.empty();
    }

    /**
     * Respond to a select first player command
     * @param arguments The command arguments
     * @return An empty optional if the command is correct, a fail if not
     */
    private Optional<String> onFirst(String[] arguments) {
        GameData data = getState().getData();

        if (arguments.length != 1) {
            return Optional.of(FIRST_FAIL);
        }

        String player = arguments[0];
        state.acceptSelectFirst(player);

        return Optional.empty();
    }

    /**
     * Print a text until a certain limit is reached without splitting words
     * @param output The string builder in use
     * @param toPrint The text to print
     * @param startOS The starting offset
     * @param limit The limit offset
     * @return The reached offset
     */
    private int printUntilLimit(StringBuilder output, List<String> toPrint, int startOS, int limit) {
        int length = toPrint.get(0).length();
        int currOS = startOS;
        while (currOS + length < limit) {
            output.append(toPrint.get(0)).append(" ");
            toPrint.remove(0);
            currOS += length + 1;
            if (toPrint.size()>0) {
                length = toPrint.get(0).length();
            } else {
                break;
            }
        }
        return currOS;
    }

}