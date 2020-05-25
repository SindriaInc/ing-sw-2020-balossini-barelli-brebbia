package it.polimi.ingsw.client.cli.view;

import it.polimi.ingsw.client.cli.CliCommand;
import it.polimi.ingsw.client.clientstates.RoomState;
import it.polimi.ingsw.common.info.RoomInfo;

import java.util.List;

public class CliRoomView extends AbstractCliView {

    private final RoomState state;

    public CliRoomView(RoomState state, int lineLength) {
        super(lineLength);

        this.state = state;
    }

    @Override
    public String generateView() {
        String name = state.getData().getName();
        RoomInfo room = state.getData().getRoom();

        StringBuilder output = new StringBuilder();

        output.append("> Logged in as ").append(name).append(System.lineSeparator());
        output.append(separator());

        int count = room.getPlayersCount();
        int max = room.getMaxPlayers();

        output.append("You are in the room: ").append(room.getOwner()).append(System.lineSeparator());
        output.append("Players (").append(count).append("/").append(max).append("): ").append(String.join(", ", room.getOtherPlayers())).append(System.lineSeparator());
        output.append("The game will start as soon as the room is filled").append(System.lineSeparator());
        output.append(separator());

        return output.toString();
    }

    @Override
    public List<CliCommand> generateCommands() {
        return List.of();
    }

}
