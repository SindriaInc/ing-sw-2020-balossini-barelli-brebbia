package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.AbstractFunctions;
import it.polimi.ingsw.client.ClientData;
import it.polimi.ingsw.client.IClient;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.event.PlayerPingEvent;
import it.polimi.ingsw.common.logging.Logger;

import java.util.function.Consumer;

public class GEndState extends AbstractClientState {

    private AbstractFunctions usableFunctions;

    private ClientData clientData;

    private Consumer<AbstractEvent> send;

    private AbstractClientState nextState = null;

    public GEndState(AbstractFunctions usableFunctions, ClientData clientData, Consumer<AbstractEvent> send) {
        this.usableFunctions = usableFunctions;
        this.clientData = clientData;
        this.send = send;
        usableFunctions.in("Write \"restart\" to restart the client or anything else to close it: ");
    }

    @Override
    public AbstractClientState nextClientState() {
        if (nextState == null) {
            return this;
        } else {
            return nextState;
        }
    }

    @Override
    public AADataTypes.Response onCommand(String message) {
        clientData.getClient().shutdown();
        clientData.setClient(null);

        if (message.equals("restart")) {
            nextState = new AInputState(usableFunctions, new ClientData(), send);
            return new AADataTypes.Response(true, null);
        }

        shutdown();
        return null;
    }

    public void shutdown() {
        Logger logger = Logger.getInstance();
        logger.info("Shutting down, goodbye!");

        IClient client = clientData.getClient();

        if (client != null) {
            client.shutdown();
        }

        logger.shutdown();
    }

}
