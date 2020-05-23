package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientstates.InputState;
import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.message.ErrorMessage;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.serializer.GsonEventSerializer;
import it.polimi.ingsw.common.serializer.SerializationException;

// TODO: Implement server timeout logic
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
     * The current state of client, implementing the available interactions
     */
    private AbstractClientState currentClientState;

    /**
     * The connection to the server, null until set
     */
    private IClient connection;

    public ClientConnector(AbstractClientViewer viewer) {
        this.viewer = viewer;
        this.currentClientState = new InputState(this);
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
    }

    public void shutdown() {
        // TODO: Implement client shutdown
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
        // TODO: Handle message sending errors
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

}
