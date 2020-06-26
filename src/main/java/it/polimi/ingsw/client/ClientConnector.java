package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.InputState;
import it.polimi.ingsw.client.message.ErrorMessage;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.event.PlayerPingEvent;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.serializer.GsonEventSerializer;
import it.polimi.ingsw.common.serializer.SerializationException;

import java.util.Timer;
import java.util.TimerTask;

import static it.polimi.ingsw.view.VirtualView.PING_SCHEDULE_MS;
import static it.polimi.ingsw.view.VirtualView.PING_TIMEOUT_MS;

public class ClientConnector {

    /**
     * The event serializer
     */
    private final GsonEventSerializer serializer = new GsonEventSerializer();

    /**
     * The client viewer, used to display the output to the user
     */
    private final AbstractClientViewer viewer;

    /**
     * The timer that manages server timeouts
     */
    private final Timer timer;

    /**
     * The current state of client, implementing the available interactions
     */
    private AbstractClientState currentClientState;

    /**
     * The connection to the server, null until set
     */
    private IClient connection;

    /**
     * The epoch of the last server ping, or null if not connected to a server
     */
    private Long lastPing = null;

    public ClientConnector(AbstractClientViewer viewer) {
        this.viewer = viewer;

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                onTimeoutCheck();
            }

        }, 0, PING_SCHEDULE_MS);

        updateState(new InputState(this));
    }

    public AbstractClientViewer getViewer() {
        return viewer;
    }

    public void registerConnection(IClient connection) {
        this.connection = connection;

        connection.registerHandler(this::onServerMessage);
        connection.registerHandler(this::onServerError);
    }

    public void updateState(AbstractClientState state) {
        this.currentClientState = state;

        state.getModelEventProvider().registerRequestPlayerPingEventObserver(event -> {
            // Reply to ping request events
            send(new PlayerPingEvent(event.getPlayer()));
            lastPing = System.currentTimeMillis();
        });
    }

    /**
     * Send an event to server
     */
    public void send(AbstractEvent event) {
        if (connection == null) {
            throw new IllegalStateException("Tried to send an event while no connection is present");
        }

        connection.send(serializer.serialize(event));
    }

    /**
     * Handle the client shutdown
     */
    public void shutdown() {
        connection.shutdown();
        timer.cancel();
        viewer.shutdown();
    }

    /**
     * Handle a message received from the server
     * @param message The message
     */
    private void onServerMessage(String message) {
        AbstractEvent event;
        try {
            event = serializer.deserialize(message);
        } catch (SerializationException exception) {
            Logger.getInstance().exception(exception);
            Logger.getInstance().warning("Received an invalid event");
            return;
        }

        try {
            event.accept(currentClientState.getModelEventProvider());
        } catch (IllegalStateException ignored) {
            // Event is not a model event
        }

        try {
            event.accept(currentClientState.getResponseEventProvider());
        } catch (IllegalStateException ignored) {
            // Event is not a model event
        }
    }

    /**
     * Handle an error occurred while sending a packet to the server
     * @param error The error
     */
    private void onServerError(ErrorMessage error) {
        connection.shutdown();
        lastPing = null;
        connection = null;
        updateState(new InputState(this, "An unexpected server error has occurred"));
    }

    private void onTimeoutCheck() {
        if (lastPing == null) {
            return;
        }

        if (System.currentTimeMillis() <= lastPing + PING_TIMEOUT_MS) {
            return;
        }

        // The server connection has timed out
        connection.shutdown();
        lastPing = null;
        connection = null;
        updateState(new InputState(this, "The connection to the server has timed out"));
    }

}
