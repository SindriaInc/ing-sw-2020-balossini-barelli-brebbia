package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientstates.AInputState;
import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.client.clientstates.AADataTypes;
import it.polimi.ingsw.client.message.ErrorMessage;
import it.polimi.ingsw.common.Observable;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidEvent;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.serializer.GsonEventSerializer;
import it.polimi.ingsw.common.serializer.SerializationException;
import it.polimi.ingsw.view.ViewEventProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Client {

    private ClientData clientData;

    private AbstractFunctions usableFunctions;

    /**
     * The event serializer
     */
    private final GsonEventSerializer serializer = new GsonEventSerializer();

    /**
     * The current state of client, implementing the available interactions
     */
    private AbstractClientState currentClientState;

    public Client(AbstractFunctions usableFunctions) {
        this.usableFunctions = usableFunctions;
        this.clientData = new ClientData();
        this.currentClientState = new AInputState(usableFunctions, clientData, message -> onMessage(message), error -> onError(error), event -> send(event));
    }

    public void setNameAndAge() {
        AADataTypes.GamerData gamerData = currentClientState.login();
        clientData.setName(gamerData.getName());
        clientData.setAge(gamerData.getAge());
    }

    public void updateClientState() {
        currentClientState = currentClientState.nextClientState();
    }

    /**
     * Execute the message
     * @param message The message
     */
    public void onMessage(String message) {

        AbstractEvent event = deserialize(message);

        try {
            event.accept(ModelEventProvider);
        } catch(Exception exception) {
            // Should never get here
            Logger.getInstance().exception(exception);
            return;
        }
    }

    /**
     * Respond to an error
     * @param error
     */
    public void onError(ErrorMessage error) {
        clientData.setInvalid(true);
        usableFunctions.out("Unexpected error while sending message, try again");
    }

    /**
     * Deserialize a message
     * @param message The message
     * @return Deserialized event
     */
    private AbstractEvent deserialize(String message) {
        AbstractEvent event;
        try {
            event = serializer.deserialize(message);
        } catch (SerializationException exception) {
            Logger.getInstance().exception(exception);
            return null;
        }
        return event;
    }

    /**
     * Serialize an event
     * @param event The event
     * @return Serialized message
     */
    private String serialize(AbstractEvent event) {
        return serializer.serialize(event);
    }

    /**
     * Send an event to server
     */
    private void send(AbstractEvent event) {
        String message = serialize(event);
        clientData.getClient().send(message);
    }

}
