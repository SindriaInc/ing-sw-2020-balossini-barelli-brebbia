package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.client.data.InputData;
import it.polimi.ingsw.client.socket.SocketClient;

import java.io.IOException;

public class InputState extends AbstractClientState {

    /**
     * The data used by this state
     */
    private InputData data;

    public InputState(ClientConnector clientConnector) {
        this(clientConnector, null);
    }

    public InputState(ClientConnector clientConnector, String message) {
        super(clientConnector);

        this.data = new InputData(message, null, null);
        updateView();
    }

    public InputData getData() {
        return data;
    }

    /**
     * Update data and view
     * @param ip The ip
     */
    public void acceptIp(String ip) {
        data = data.withIp(ip);
        updateView();
    }

    /**
     * Update data and view
     * @param port The port
     */
    public void acceptPort(int port) {
        data = data.withPort(port);
        updateView();
    }

    /**
     * Try a connection to the server
     */
    public void acceptConnect() {
        if (data.getIp().isEmpty() || data.getPort().isEmpty()) {
            throw new IllegalStateException("Tried to connect with a null ip or port");
        }

        try {
            SocketClient socket = new SocketClient(data.getIp().get(), data.getPort().get());
            getClientConnector().registerConnection(socket);
            getClientConnector().updateState(new LoginState(getClientConnector()));
        } catch (IllegalArgumentException | IOException exception) {
            data = new InputData("Unable to connect to the server: " + exception.getMessage(), null, null);
            updateView();
        }
    }

    /**
     * Update the view
     */
    private void updateView() {
        getClientConnector().getViewer().viewInput(this);
    }

}
