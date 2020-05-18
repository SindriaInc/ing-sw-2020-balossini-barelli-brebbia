package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.AbstractFunctions;
import it.polimi.ingsw.client.ClientData;
import it.polimi.ingsw.client.cli.CliMain;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.event.PlayerPingEvent;
import it.polimi.ingsw.common.event.lobby.LobbyRoomUpdateEvent;
import it.polimi.ingsw.model.Game;

import java.util.function.Consumer;

public class DRoomState extends AbstractClientState {

    private AbstractFunctions usableFunctions;

    private ClientData clientData;

    private Consumer<AbstractEvent> send;

    private AbstractClientState nextState = null;

    public DRoomState(AbstractFunctions usableFunctions, ClientData clientData, Consumer<AbstractEvent> send) {
        this.usableFunctions = usableFunctions;
        this.clientData = clientData;
        this.send = send;
    }

    @Override
    public void onLobbyRoomUpdateEvent(AbstractEvent event) {
        clientData.setRoom(((LobbyRoomUpdateEvent) event).getRoomInfo());

        usableFunctions.printRoom(clientData);
    }

    @Override
    public void onLobbyGameStartEvent(AbstractEvent event) {
        for (int x = 0; x < Game.BOARD_COLUMNS; x++) {
            for (int y = 0; y < Game.BOARD_ROWS; y++) {
                clientData.getMap()[x][y] = new AADataTypes.CellInfo(x, y);
            }
        }

        usableFunctions.printBoard(clientData, AADataTypes.InGame.IN_GAME, null);
        nextState = new EGameState(usableFunctions, clientData, send);
    }

    @Override
    public AbstractClientState nextClientState() {
        if (nextState != null) {
            return nextState;
        }
        return this;
    }

    @Override
    public AADataTypes.Response onCommand(String message) {
        return new AADataTypes.Response(false, "Be patient, the game is about to start");
    }
}
