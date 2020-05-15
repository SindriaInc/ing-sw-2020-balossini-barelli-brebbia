package it.polimi.ingsw.view;

import it.polimi.ingsw.common.*;
import it.polimi.ingsw.common.event.AbstractEvent;
import it.polimi.ingsw.common.event.PlayerLoginEvent;
import it.polimi.ingsw.common.event.PlayerLogoutEvent;
import it.polimi.ingsw.common.event.PlayerPingEvent;
import it.polimi.ingsw.common.event.lobby.AbstractLobbyEvent;
import it.polimi.ingsw.common.event.request.AbstractRequestEvent;
import it.polimi.ingsw.common.event.request.RequestPlayerPingEvent;
import it.polimi.ingsw.common.event.response.AbstractResponseEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidParametersEvent;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.serializer.GsonEventSerializer;
import it.polimi.ingsw.common.serializer.SerializationException;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.server.message.ErrorMessage;
import it.polimi.ingsw.server.message.InboundMessage;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class VirtualView {

    private static final long PING_SCHEDULE_MS = 10000;

    private static final long PING_TIMEOUT_MS = PING_SCHEDULE_MS * 3;

    private final IServer server;

    private final IPlayerChecker playerChecker;

    private final ViewEventProvider viewEventProvider;

    private final GsonEventSerializer eventSerializer;

    private final Map<String, Long> lastPlayerPings = new ConcurrentHashMap<>();

    private final Timer timer;

    private final Validator playerNameValidator = new Validator(Validator.PLAYER_NAME);

    public VirtualView(IServer server, IPlayerChecker playerChecker, IResponseEventProvider responseEventProvider) {
        this.server = server;
        this.playerChecker = playerChecker;

        server.registerHandler(this::onConnect);
        server.registerHandler(this::onMessage);
        server.registerHandler(this::onError);

        responseEventProvider.registerResponseInvalidPlayerEventObserver(this::onResponse);
        responseEventProvider.registerResponseInvalidParametersEventObserver(this::onResponse);
        responseEventProvider.registerResponseInvalidStateEventObserver(this::onResponse);

        viewEventProvider = new ViewEventProvider();
        eventSerializer = new GsonEventSerializer();

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                onPingTick();
            }

        }, 0, PING_SCHEDULE_MS);
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
        modelEventProvider.registerWorkerMoveEventObserver(this::onEvent);
        modelEventProvider.registerWorkerBuildBlockEventObserver(this::onEvent);
        modelEventProvider.registerWorkerBuildDomeEventObserver(this::onEvent);
        modelEventProvider.registerWorkerForceEventObserver(this::onEvent);
        modelEventProvider.registerWorkerSpawnEventObserver(this::onEvent);
    }

    public ViewEventProvider getViewEventProvider() {
        return viewEventProvider;
    }

    public void shutdown() {
        timer.cancel();
    }

    private void onPingTick() {
        for (Map.Entry<String, Long> lastPlayerPing : lastPlayerPings.entrySet()) {
            String player = lastPlayerPing.getKey();
            long time = lastPlayerPing.getValue();

            if (System.currentTimeMillis() >= time + PING_TIMEOUT_MS) {
                // Player timed out
                disconnect(player);
                continue;
            }

            server.send(new OutboundMessage(player, serializedEvent(new RequestPlayerPingEvent(player))));
        }
    }

    private void onConnect(String tempName) {
        lastPlayerPings.put(tempName, System.currentTimeMillis());
    }

    private void onMessage(InboundMessage message) {
        AbstractEvent event;

        try {
            event = eventSerializer.deserialize(message.getMessage());
        } catch (SerializationException exception) {
            // Bad client
            Logger.getInstance().warning("Invalid message received: " + exception.getMessage());
            sendInvalidResponse(message.getSourcePlayer());
            return;
        }

        String player = message.getSourcePlayer();

        if (event instanceof PlayerPingEvent) {
            // Player replied to a ping event
            lastPlayerPings.put(player, System.currentTimeMillis());
            return;
        }

        if (event instanceof PlayerLogoutEvent) {
            // Player wants to disconnect
            disconnect(player);
            return;
        }

        if (!IServer.isIdentified(player)) {
            // The player has not identified, the only packet that can be sent is a PlayerLoginEvent

            try {
                PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;
                player = playerLoginEvent.getPlayer();

                if (!playerNameValidator.isValid(player) || !playerChecker.isNameAvailable(player) || playerLoginEvent.getAge() <= 0) {
                    // Invalid name or age
                    sendInvalidResponse(message.getSourcePlayer());
                    return;
                }

                message.onLogin(player);

                // Replace the temporary identifier with a new one
                lastPlayerPings.remove(message.getSourcePlayer());
                lastPlayerPings.put(player, System.currentTimeMillis());
            } catch (ClassCastException exception) {
                // Bad client
                Logger.getInstance().warning("Invalid event received while logging in: " + exception.getMessage());
                sendInvalidResponse(message.getSourcePlayer());
                return;
            }
        } else if (event.getSender().isEmpty() || !player.equals(event.getSender().get())) {
            // Bad client
            Logger.getInstance().warning("Invalid player in event: Got " + event.getSender() + ", expected " + message.getSourcePlayer());
            sendInvalidResponse(message.getSourcePlayer());
            return;
        }

        handleEvent(player, event.getClass().getSimpleName(), event);
    }

    private void onError(ErrorMessage message) {
        Optional<OutboundMessage> outboundMessage = message.getOutboundMessage();

        if (outboundMessage.isEmpty()) {
            return;
        }

        // Disconnect the player
        String player = outboundMessage.get().getDestinationPlayer();
        disconnect(player);
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

    private void disconnect(String player) {
        server.disconnect(player);
        handleEvent(player, PlayerLogoutEvent.class.getSimpleName(), new PlayerLogoutEvent(player));
        lastPlayerPings.remove(player);
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

    private void sendInvalidResponse(String player) {
        String serialized = serializedEvent(new ResponseInvalidParametersEvent(player));
        server.send(new OutboundMessage(player, serialized));
    }

}
