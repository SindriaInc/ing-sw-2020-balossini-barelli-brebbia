package it.polimi.ingsw.client.cli.view.game;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.cli.CliConstants;
import it.polimi.ingsw.client.clientstates.GameState;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.common.info.GodInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.client.cli.CliConstants.*;

public class CliGodsView extends AbstractGameView {

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

    private final GameState state;

    public CliGodsView(GameState state, int lineLength) {
        super(state, lineLength);

        this.state = super.getState();
    }

    @Override
    public String generateView() {
        List<GodInfo> gods;
        StringBuilder output = new StringBuilder();

        if (state.getData().getSelectGodsData().isPresent()) {
            gods = state.getData().getSelectGodsData().get().getAvailableGods();
        } else if (state.getData().getChooseGodData().isPresent()) {
            gods = state.getData().getChooseGodData().get().getAvailableGods();
        } else {
            return "Waiting for your turn...";
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

    @Override
    public List<CliCommand> generateCommands() {
        GameData data = getState().getData();

        if (state.getData().getTurnPlayer().isPresent() &&
                state.getData().getTurnPlayer().get().equals(state.getData().getName()) &&
                !state.getData().isSpectating()) {
            if (data.getSelectGodsData().isPresent()) {
                String[] args;
                if (state.getData().getOtherPlayers().size() + 1 == 2) {
                    args = new String[]{"<God1>", "<God 2>"};
                } else if (state.getData().getOtherPlayers().size() + 1 == 3) {
                    args = new String[]{"<God1>", "<God 2>", "<God 3>"};
                } else {
                    args = new String[]{"<God1>", "<God 2>", "<God 3>", "<God 4>"};
                }

                return List.of(
                        new CliCommand("select", args, "Select the gods to use in this game", this::onSelect)
                );
            } else if (data.getChooseGodData().isPresent()) {
                return List.of(
                        new CliCommand("choose", new String[]{"<GodID>"}, "Choose a god", this::onChoose)
                );
            }
        }

        return List.of();
    }

    private Optional<String> onSelect(String[] arguments) {
        if (arguments.length != state.getData().getOtherPlayers().size() + 1) {
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

    private Optional<String> onChoose(String[] arguments) {
        if (arguments.length != 1) {
            return Optional.of(CHOOSE_FAIL);
        }

        if (arguments[0].length() <= 0) {
            return  Optional.of(CHOOSE_FAIL);
        }

        state.acceptChooseGod(arguments[0]);

        return Optional.empty();
    }

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