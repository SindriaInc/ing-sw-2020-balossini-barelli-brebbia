package it.polimi.ingsw.view;

import it.polimi.ingsw.common.IModelEventProvider;
import it.polimi.ingsw.common.IResponseEventProvider;
import it.polimi.ingsw.common.Observable;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.event.PlayerLoginEvent;
import it.polimi.ingsw.common.event.lobby.AbstractLobbyEvent;
import it.polimi.ingsw.common.event.request.AbstractRequestEvent;
import it.polimi.ingsw.common.event.response.AbstractResponseEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidEvent;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.serializer.GsonEventSerializer;
import it.polimi.ingsw.common.serializer.SerializationException;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.server.message.ErrorMessage;
import it.polimi.ingsw.server.message.InboundMessage;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VirtualView {

    private final IServer server;

    private final ViewEventProvider viewEventProvider;

    private final GsonEventSerializer eventSerializer;

    public VirtualView(IServer server, IResponseEventProvider responseEventProvider) {
        this.server = server;

        server.registerHandler(this::onMessage);
        server.registerHandler(this::onError);

        responseEventProvider.registerResponseInvalidPlayerEventObserver(this::onResponse);
        responseEventProvider.registerResponseInvalidParametersEventObserver(this::onResponse);
        responseEventProvider.registerResponseInvalidStateEventObserver(this::onResponse);

        viewEventProvider = new ViewEventProvider();
        eventSerializer = new GsonEventSerializer();
    }

    public void selectModelEventProvider(IModelEventProvider modelEventProvider) {
        modelEventProvider.registerLobbyUpdateEventObserver(this::onLobbyEvent);
        modelEventProvider.registerLobbyRoomUpdateEventObserver(this::onLobbyEvent);
        modelEventProvider.registerLobbyGameStartEventObserver(this::onLobbyEvent);

        modelEventProvider.registerPlayerRequestChallengerSelectGodsEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestPlayerChooseGodEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestPlayerEndTurnEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestWorkerBuildBlockEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestWorkerBuildDomeEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestWorkerForceEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestWorkerMoveEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestWorkerSpawnEventObserver(this::onRequestEvent);

        modelEventProvider.registerPlayerChallengerSelectGodsEventObserver(this::onEvent);
        modelEventProvider.registerPlayerChooseGodEventObserver(this::onEvent);
        modelEventProvider.registerPlayerLoseEventObserver(this::onEvent);
        modelEventProvider.registerPlayerTurnStartEventObserver(this::onEvent);
        modelEventProvider.registerPlayerWinEventObserver(this::onEvent);
        modelEventProvider.registerWorkerBuildBlockEventObserver(this::onEvent);
        modelEventProvider.registerWorkerBuildDomeEventObserver(this::onEvent);
        modelEventProvider.registerWorkerForceEventObserver(this::onEvent);
        modelEventProvider.registerWorkerSpawnEventObserver(this::onEvent);
    }

    public ViewEventProvider getViewEventProvider() {
        return viewEventProvider;
    }

    private void onMessage(InboundMessage message) {
        AbstractEvent event;

        try {
            event = eventSerializer.deserialize(message.getMessage());
        } catch (SerializationException exception) {
            // Bad client
            Logger.getInstance().warning("Invalid message received: " + exception.getMessage());
            // TODO: message.dispatchResponse(...)
            return;
        }

        String player;

        if (message.getSourcePlayer().isEmpty()) {
            // The player has not identified, the only packet that can be sent is a PlayerLoginEvent

            try {
                PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;
                player = playerLoginEvent.getPlayer();
                message.onLogin(player);
            } catch (ClassCastException exception) {
                // Bad client
                Logger.getInstance().warning("Invalid event received while logging in: " + exception.getMessage());
                // TODO: message.dispatchResponse(...)
                return;
            }
        } else {
            if (!message.getSourcePlayer().equals(event.getSender())) {
                // Bad client
                Logger.getInstance().warning("Invalid player in event: Got " + event.getSender() + ", expected " + message.getSourcePlayer());
                return;
            }

            player = message.getSourcePlayer().get();
        }

        handleEvent(player, event.getClass().getSimpleName(), event);
    }

    private void onError(ErrorMessage message) {
        // TODO: Implement error handling
        // Reschedule messages if needed
    }

    @SuppressWarnings("unchecked")
    private void handleEvent(String player, String eventName, AbstractEvent event) {
        // The getter for the observable related to the event
        Method observableMethod;

        try {
            observableMethod = ViewEventProvider.class.getDeclaredMethod("get" + eventName + "Observable");
        } catch (NoSuchMethodException exception) {
            // Should never get here unless ViewEventProvider is badly implemented
            Logger.getInstance().exception(exception);
            onResponse(new ResponseInvalidEvent(player));
            return;
        }

        // The observable related to the event
        Observable<AbstractEvent> observable;

        try {
            observable = (Observable<AbstractEvent>) observableMethod.invoke(viewEventProvider);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            // Should never get here since we're calling a getter
            Logger.getInstance().exception(exception);
            onResponse(new ResponseInvalidEvent(player));
            return;
        }

        observable.notifyObservers(event);
    }

    private void onLobbyEvent(AbstractLobbyEvent event) {
        String serialized = serializedEvent(event);
        dispatchEvent(event.getPlayer(), serialized);
    }

    private void onRequestEvent(AbstractRequestEvent event) {
        String serialized = serializedEvent(event);
        dispatchEvent(event.getPlayer(), serialized);
    }

    private void onResponse(AbstractResponseEvent event) {
        String serialized = serializedEvent(event);
        dispatchEvent(event.getPlayer(), serialized);
    }

    private void onEvent(AbstractEvent event) {
        dispatchEvent(event);
    }

    private void dispatchEvent(AbstractEvent event) {
        String serialized = serializedEvent(event);

        if (event.getReceivers().isEmpty()) {
            Logger.getInstance().warning("Trying to dispatch an event with no receivers: " + event.getClass().getSimpleName());
            return;
        }

        for (String player : event.getReceivers().get()) {
            dispatchEvent(player, serialized);
        }
    }

    private void dispatchEvent(String player, String serialized) {
        server.send(new OutboundMessage(player, serialized));
    }

    private String serializedEvent(AbstractEvent event) {
        return eventSerializer.serialize(event);
    }

}
