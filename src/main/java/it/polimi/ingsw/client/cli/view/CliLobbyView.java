package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.clientstates.LobbyState;
import it.polimi.ingsw.common.info.RoomInfo;

import java.util.List;
import java.util.Optional;

public class CliLobbyView extends AbstractCliView {

    private static final String TYPE_SIMPLE = "simple";
    private static final String TYPE_NORMAL = "normal";

    private static final String JOIN_ARGUMENTS_FAIL = "Please type the name of the room you want to join (Example: join CoolPlayer20)";
    private static final String CREATE_ARGUMENTS_FAIL = "Please type the type of game you want and the number of players (Example: create " + TYPE_NORMAL + " 3)";

    private static final String SIDE_PADDING = " ".repeat(12);

    private final LobbyState state;
    private final String[] side;

    public CliLobbyView(LobbyState state, String[] side, int lineLength) {
        super(lineLength);

        this.state = state;
        this.side = side;
    }

    @Override
    public String generateView() {
        String name = state.getData().getName();
        List<RoomInfo> rooms = state.getData().getRooms();
        Optional<String> lastMessage = state.getData().getLastMessage();
        int largestNameLength = largestLine(rooms.stream().map(RoomInfo::getOwner).toArray(String[]::new));
        int sideLineLength = largestLine(side);
        int startingLine = Math.max(0, (side.length - (rooms.size() + 2)) / 2);
        boolean noMoreRooms = false;

        StringBuilder output = new StringBuilder();

        output.append("> Logged in as ").append(name).append(System.lineSeparator());
        output.append(separator()).append(System.lineSeparator());

        int i;
        for (i = 0; i < side.length; i++) {
            String line = side[i];
            output.append(SIDE_PADDING).append(line).append(" ".repeat(sideLineLength - line.length()));

            if (rooms.size() == 0) {
                if (i == (side.length / 2) - 1) {
                    output.append(leftPadAndCenter("No room available", sideLineLength + SIDE_PADDING.length()));
                } else if (i == (side.length / 2) + 1) {
                    output.append(leftPadAndCenter("Please create a new one", sideLineLength + SIDE_PADDING.length()));
                }
                noMoreRooms = true;
            } else {
                if (i == startingLine) {
                    output.append(leftPadAndCenter("AVAILABLE ROOMS", sideLineLength + SIDE_PADDING.length()));
                } else if (i > startingLine + 1 && (i - startingLine - 2) < rooms.size() ) {
                    RoomInfo roomInfo = rooms.get(i - startingLine - 2);
                    output.append(buildRoom(sideLineLength, largestNameLength, roomInfo));
                } else if (i != startingLine + 1) {
                    noMoreRooms = true;
                }
            }

            output.append(System.lineSeparator());
        }

        if (!noMoreRooms) {
            for (i -= side.length - 2; i<rooms.size(); i++) {
                output.append(" ".repeat(side[0].length()));
                RoomInfo roomInfo = rooms.get(i);
                output.append(buildRoom(sideLineLength, largestNameLength, roomInfo));
            }
        }

        output.append(System.lineSeparator()).append(separator());

        lastMessage.ifPresent(string -> output.append(center(string)).append(System.lineSeparator()));

        return output.toString();
    }

    @Override
    public List<CliCommand> generateCommands() {
        if (state.getData().isWaiting()) {
            return List.of();
        }

        return List.of(
                new CliCommand("join", new String[]{"<name>"}, "Join an existing room", this::onJoin),
                new CliCommand("create", new String[]{"<" + TYPE_SIMPLE + "|" + TYPE_NORMAL + ">", "<max>"}, "Create a new room (for a simple or normal game)", this::onCreate)
        );
    }

    private Optional<String> onJoin(String[] arguments) {
        if (arguments.length != 1) {
            return Optional.of(JOIN_ARGUMENTS_FAIL);
        }

        String name = arguments[0];

        if (name.length() <= 0) {
            return Optional.of(JOIN_ARGUMENTS_FAIL);
        }

        state.acceptJoin(arguments[0]);
        return Optional.empty();
    }

    private Optional<String> onCreate(String[] arguments) {
        if (arguments.length != 2) {
            return Optional.of(CREATE_ARGUMENTS_FAIL);
        }

        String type = arguments[0];
        boolean simple = false;

        if (type.equals(TYPE_SIMPLE)) {
            simple = true;
        } else if (!type.equals(TYPE_NORMAL)) {
            return Optional.of("Invalid type \"" + type + "\", the type must be either \"" + TYPE_SIMPLE + "\" or \"" + TYPE_NORMAL + "\"");
        }

        try {
            state.acceptCreate(simple, Integer.parseInt(arguments[1]));
            return Optional.empty();
        } catch (NumberFormatException exception) {
            return Optional.of("The number of players must be... A number");
        }
    }

    private String buildRoom(int sideLineLen, int largestNameLength, RoomInfo roomInfo) {
        StringBuilder output = new StringBuilder();

        int count = roomInfo.getPlayersCount();
        int max = roomInfo.getMaxPlayers();
        String simple = roomInfo.isSimpleGame() ? "Y" : "N";

        output.append("> Name: ").append(roomInfo.getOwner())
                .append(" ".repeat(largestNameLength - roomInfo.getOwner().length()))
                .append(" | Players: ").append(count).append("/").append(max)
                .append(", Simple: ").append(simple);

        return leftPadAndCenter(output.toString(), sideLineLen + SIDE_PADDING.length());
    }

}
