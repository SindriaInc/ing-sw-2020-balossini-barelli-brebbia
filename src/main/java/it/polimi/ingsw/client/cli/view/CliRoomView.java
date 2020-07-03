package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.clientstates.RoomState;
import it.polimi.ingsw.common.info.RoomInfo;

import java.util.List;
import java.util.Optional;

/**
 * Generate a vli room view
 */
public class CliRoomView extends AbstractCliView {

    private static final String SIDE_PADDING = " ".repeat(12);

    /**
     * The clien state
     */
    private final RoomState state;

    /**
     * The side banner
     */
    private final String[] side;

    /**
     * Class constructor, set the client state, the side banner and the line length
     *
     * @param state The client state
     * @param side The side banner
     * @param lineLength The line length
     */
    public CliRoomView(RoomState state, String[] side, int lineLength) {
        super(lineLength);

        this.state = state;
        this.side = side;
    }

    /**
     * @see AbstractCliView#generateView()
     */
    @Override
    public String generateView() {
        String name = state.getData().getName();
        RoomInfo room = state.getData().getRoom();
        Optional<String> lastMessage = state.getData().getLastMessage();
        int sideLineLength = largestLine(side);
        int count = room.getPlayersCount();
        int max = room.getMaxPlayers();


        StringBuilder output = new StringBuilder();

        output.append("> Logged in as ").append(name).append(System.lineSeparator());
        output.append(separator()).append(System.lineSeparator());

        int i;
        for (i = 0; i < side.length; i++) {
            String line = side[i];
            output.append(SIDE_PADDING).append(line).append(" ".repeat(sideLineLength - line.length()));

            if (i == 6) {
                output.append(leftPadAndCenter("You are in the room: " + room.getOwner(), sideLineLength + SIDE_PADDING.length()));
            } else if (i == 8){
                output.append(leftPadAndCenter("Players (" + count + "/" + max + "): " +
                        String.join(", ", room.getOtherPlayers()), sideLineLength + SIDE_PADDING.length()));
            } else if (i == 10) {
                output.append(leftPadAndCenter("The game will start as soon as the room is filled", sideLineLength + SIDE_PADDING.length()));
            }

            output.append(System.lineSeparator());
        }

        output.append(System.lineSeparator()).append(separator());

        lastMessage.ifPresent(string -> output.append(center(string)).append(System.lineSeparator()));

        return output.toString();

    }

    /**
     * @see AbstractCliView#generateCommands()
     */
    @Override
    public List<CliCommand> generateCommands() {
        return List.of();
    }

}
