package it.polimi.ingsw.client.clientstates;


import it.polimi.ingsw.client.AbstractFunctions;
import it.polimi.ingsw.client.ClientData;
import it.polimi.ingsw.client.cli.CliMain;
import it.polimi.ingsw.common.RoomInfo;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.event.PlayerCreateRoomEvent;
import it.polimi.ingsw.common.event.PlayerJoinRoomEvent;
import it.polimi.ingsw.common.event.PlayerPingEvent;
import it.polimi.ingsw.common.event.lobby.LobbyRoomUpdateEvent;
import it.polimi.ingsw.common.event.lobby.LobbyUpdateEvent;
import it.polimi.ingsw.model.Room;

import static it.polimi.ingsw.client.cli.CliConstants.*;

import java.util.function.Consumer;

public class CLobbyState extends AbstractClientState {

    private AbstractFunctions usableFunctions;

    private ClientData clientData;

    private Consumer<AbstractEvent> send;

    private AbstractClientState nextState = null;

    public CLobbyState(AbstractFunctions usableFunctions, ClientData clientData, Consumer<AbstractEvent> send) {
        this.usableFunctions = usableFunctions;
        this.clientData = clientData;
        this.send = send;
    }

    @Override
    public AbstractClientState nextClientState() {
        if (nextState != null) {
            return nextState;
        }
        return this;
    }

    @Override
    public void onLobbyUpdateEvent(AbstractEvent event) {
        clientData.getRooms().clear();
        clientData.getRooms().addAll(((LobbyUpdateEvent) event).getRooms());

        usableFunctions.printLobby(clientData);
    }

    @Override
    public void onLobbyRoomUpdateEvent(AbstractEvent event) {
        clientData.setRoom(((LobbyRoomUpdateEvent) event).getRoomInfo());

        usableFunctions.printRoom(clientData);
        nextState = new DRoomState(usableFunctions, clientData, send);
    }

    @Override
    public void onResponseInvalidParametersEvent(AbstractEvent event) {
        usableFunctions.out("Invalid command parameters");
    }

    @Override
    public AADataTypes.Response onCommand(String message) {

        String command = usableFunctions.in("Insert command: ");
        String[] parameters = command.split(":");

        if (parameters.length <= 0) {
            return new AADataTypes.Response(false, "Did you really get here?");
        }

        String label = parameters[0];

        if (label.equals(COMMAND_CREATE)) {
            if (parameters.length != 3) {
                return new AADataTypes.Response(false, "Invalid parameters (Expected 3, got " + parameters.length + ")");
            }

            Integer maxPlayers = intOrNull(parameters[1]);

            if (maxPlayers == null) {
                return new AADataTypes.Response(false, "Invalid max players, must be a number");
            }

            boolean simple;

            if (parameters[2].equalsIgnoreCase("simple")) {
                simple = true;
            } else if (parameters[2].equalsIgnoreCase("normal")) {
                simple = false;
            } else {
                return new AADataTypes.Response(false, "Invalid game type, must be either simple or normal");
            }

            send.accept(new PlayerCreateRoomEvent(clientData.getName(), maxPlayers, simple));
        } else if (label.equals(COMMAND_JOIN)) {
            if (parameters.length != 2) {
                return new AADataTypes.Response(false, "Invalid parameters (Expected 2, got " + parameters.length + ")");
            }

            String owner = parameters[1];

            send.accept(new PlayerJoinRoomEvent(clientData.getName(), owner));
        } else {
            return new AADataTypes.Response(false, "Invalid command");
        }

        return new AADataTypes.Response(false, "Don't think anyone can get here... Unless you blow up everything");
    }

    private Integer intOrNull(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

}