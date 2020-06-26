package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.controller.ResponseEventProvider;
import it.polimi.ingsw.model.ModelEventProvider;

/**
 * A clientConnector state is meant to handle interactions between the user and the server
 * All the clientConnector logic should be implemented here
 */
public abstract class AbstractClientState {

    /**
     * The clientConnector instance
     */
    private final ClientConnector clientConnector;

    /**
     * The model event provider, used by the concrete states to listen for events
     */
    private final ModelEventProvider modelEventProvider;

    /**
     * The model event provider, used by the concrete states to listen for response events (rejects by the server)
     */
    private final ResponseEventProvider responseEventProvider;

    protected AbstractClientState(ClientConnector clientConnector) {
        this.clientConnector = clientConnector;

        // Initialize new event providers, this way the clientConnector will only send events to the current state
        responseEventProvider = new ResponseEventProvider();
        modelEventProvider = new ModelEventProvider();
    }

    public ClientConnector getClientConnector() {
        return clientConnector;
    }

    public ModelEventProvider getModelEventProvider() {
        return modelEventProvider;
    }

    public ResponseEventProvider getResponseEventProvider() {
        return responseEventProvider;
    }

    public void shutdown() {
        clientConnector.shutdown();
    }

}
