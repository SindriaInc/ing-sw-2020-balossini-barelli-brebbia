package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.AbstractFunctions;
import it.polimi.ingsw.client.ClientData;
import it.polimi.ingsw.client.socket.SocketClient;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.event.PlayerLoginEvent;
import it.polimi.ingsw.common.event.PlayerPingEvent;
import it.polimi.ingsw.common.event.lobby.LobbyUpdateEvent;

import java.util.function.Consumer;

public class BLoginState extends AbstractClientState {

    private enum Phase {
        WAIT_NAME,
        WAIT_AGE,
        WAIT_LOGIN
    }

    /**
     * The phase of login state
     */
    private Phase phase = Phase.WAIT_NAME;

    /**
     * The name
     */
    private String name;

    /**
     * The age
     */
    private int age;

    /**
     * The next state
     * While null the client remain in login state
     */
    private AbstractClientState nextState = null;

    private AbstractFunctions usableFunctions;

    private ClientData clientData;

    private Consumer<AbstractEvent> send;

    public BLoginState(AbstractFunctions usableFunctions, ClientData clientData, Consumer<AbstractEvent> send) {
        this.usableFunctions = usableFunctions;
        this.clientData = clientData;
        this.send = send;
    }

    @Override
    public AbstractClientState nextClientState() {
        if (nextState == null) {
            return this;
        }
        return nextState;
    }

    @Override
    public void onLobbyUpdateEvent(AbstractEvent event) {
        clientData.getRooms().clear();
        clientData.getRooms().addAll(((LobbyUpdateEvent) event).getRooms());

        usableFunctions.printLobby(clientData);
        nextState = new CLobbyState(usableFunctions, clientData, send);
        clientData.setName(name);
        clientData.setAge(age);
        super.setPlayerName(name);
    }

    @Override
    public void onResponseInvalidParametersEvent(AbstractEvent event) {
        phase = Phase.WAIT_NAME;
        usableFunctions.out("Invalid parameters. Retry");
    }

    @Override
    public AADataTypes.Response onCommand(String command) {
        if (phase == Phase.WAIT_NAME) {
            name = command;
            phase = Phase.WAIT_AGE;
            return new AADataTypes.Response(true, null);
        }

        if (phase == Phase.WAIT_AGE) {
            try {
                age = Integer.parseInt(command);
            } catch (Exception e) {
                return new AADataTypes.Response(false, "Age must be a number");
            }
            phase = Phase.WAIT_LOGIN;
            return new AADataTypes.Response(true, null);
        }

        if (phase == Phase.WAIT_LOGIN) {
            return new AADataTypes.Response(false, "Just wait a bit more. Login in progress");
        }

        return new AADataTypes.Response(false, "Hope to never reach this message :)");
    }
}
