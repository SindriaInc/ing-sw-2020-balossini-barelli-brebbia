package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.ClientData;
import it.polimi.ingsw.client.IClient;
import it.polimi.ingsw.client.AbstractFunctions;
import it.polimi.ingsw.client.cli.CliMain;
import it.polimi.ingsw.client.message.ErrorMessage;
import it.polimi.ingsw.client.socket.SocketClient;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.event.PlayerPingEvent;
import it.polimi.ingsw.common.event.request.RequestPlayerPingEvent;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class AInputState extends AbstractClientState {

    private enum Phase {
        WAIT_IP,
        WAIT_PORT,
        WAIT_CONNECTION
    }

    /**
     * The phase
     */
    private Phase phase;

    /**
     * The IP address
     */
    private String ip;

    /**
     * The port
     */
    private int port;

    /**
     * The next state
     * While null the client remain in inputState
     */
    private AbstractClientState nextState = null;

    /**
     * Used UI functions
     */
    private AbstractFunctions usableFunctions;

    /**
     * The function called when a message arrive
     */
    private Consumer<String> onMessage;

    /**
     * THe function called when an error occurr
     */
    private Consumer<ErrorMessage> onError;

    /**
     * The function used to send a message to server
     */
    private Consumer<AbstractEvent> send;

    /**
     * The instance containing all client's data
     */
    private ClientData clientData;

    public AInputState(AbstractFunctions usableFunctions, ClientData clientData, Consumer<String> onMessage,
                       Consumer<ErrorMessage> onError, Consumer<AbstractEvent> send) {
        this.usableFunctions = usableFunctions;
        this.clientData = clientData;
        this.onMessage = onMessage;
        this.onError = onError;
        this.send = send;
        firstPrint();
    }

    public AInputState(AbstractFunctions usableFunctions, ClientData clientData, Consumer<AbstractEvent> send) {
        this.usableFunctions = usableFunctions;
        this.clientData = clientData;
        this.send = send;
        super.setSend(send);
        firstPrint();
    }

    private void firstPrint() {
        usableFunctions.out("Server IP: ");
    }

    /**
     * States in which state the client will be next
     * @return The next state
     */
    @Override
    public AbstractClientState nextClientState() {
        if (nextState == null) {
            return this;
        }
        return nextState;
    }

    @Override
    public void onConnect() {
        super.onConnect();
    }

    public AADataTypes.Response onCommand(String command) {
        if (phase == Phase.WAIT_IP) {
            ip = command;
            phase = Phase.WAIT_PORT;
            usableFunctions.out("Server port");
            return new AADataTypes.Response(true, null);
        }

        if (phase == Phase.WAIT_PORT) {
            try {
                port = Integer.parseInt(command);
            } catch (Exception e) {
                return new AADataTypes.Response(false, "Port must be a number");
            }
            phase = Phase.WAIT_CONNECTION;
            return new AADataTypes.Response(true, null);
        }

        if (phase == Phase.WAIT_CONNECTION) {
            try {
                SocketClient socket = new SocketClient(ip, port);
                clientData.setClient(socket);
                nextState = new BLoginState(usableFunctions, clientData, send);
                return new AADataTypes.Response(true, null);
            } catch (Exception e) {
                phase = Phase.WAIT_IP;
                firstPrint();
                return new AADataTypes.Response(false, "Wrong IP or port");
            }
        }

        return new AADataTypes.Response(false, "Well, if you see this message something's reeeeeeaaaaaaaly wrong :(");
    }
}
