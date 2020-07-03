package it.polimi.ingsw.view;

import it.polimi.ingsw.common.IModelEventProvider;
import it.polimi.ingsw.common.IPlayerChecker;
import it.polimi.ingsw.common.IResponseEventProvider;
import it.polimi.ingsw.common.Validator;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.lobby.AbstractLobbyEvent;
import it.polimi.ingsw.common.event.request.AbstractRequestEvent;
import it.polimi.ingsw.common.event.request.RequestPlayerPingEvent;
import it.polimi.ingsw.common.event.response.AbstractResponseEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidLoginEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidParametersEvent;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.common.serializer.GsonEventSerializer;
import it.polimi.ingsw.common.serializer.SerializationException;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.server.message.ErrorMessage;
import it.polimi.ingsw.server.message.InboundMessage;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The VirtualView provides the controller an interface to communicate with the client
 *
 * It handles event handling/dispatching and has access to the server connector implementation
 */
public class VirtualView {

    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 20;
    private static final String PLAYER_NAME = "^[a-zA-Z0-9_-][a-zA-Z0-9_ .-]{" + (MIN_NAME_LENGTH - 2) + "," + (MAX_NAME_LENGTH - 2) + "}[a-zA-Z0-9_-]$";

    public static final long PING_SCHEDULE_MS = 1000;
    public static final long PING_TIMEOUT_MS = PING_SCHEDULE_MS * 3;

    /**
     * The server instance, handling player connections and sending/receiving packets
     */
    private final IServer server;

    /**
     * The player checker, needed to verify the player name and age before logging in
     */
    private final IPlayerChecker playerChecker;

    /**
     * The ViewEventProvider, used to register and send view events
     */
    private final ViewEventProvider viewEventProvider;

    /**
     * The event serializer
     */
    private final GsonEventSerializer eventSerializer;

    /**
     * Map having players as keys (or their temporary fake name) and the latest ping event received (measured using epoch milliseconds time)
     * If the last ping is older than [CURRENT TIME] + [PING_TIMEOUT_MS], the player will be forcibly disconnected
     */
    private final Map<String, Long> lastPlayerPings = new ConcurrentHashMap<>();

    /**
     * The timer used to regurarly send pings and check for timeouts
     */
    private final Timer timer;

    /**
     * The player name validator, used to check for invalid characters
     */
    private final Validator playerNameValidator = new Validator(PLAYER_NAME);

    /**
     * Class constructor, registers the handlers for server events and initializes a timer for timeout
     *
     * @param server The server connector instance
     * @param playerChecker The instance of the player name and age checker
     * @param responseEventProvider The provider for response events sent by the controller
     */
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

        // Register the VirtualView to listen for login, ping, and logout events
        // Since these events got registered first, the VirtualView will be the first to read them
        viewEventProvider.registerPlayerLoginEventObserver(this::onLogin);
        viewEventProvider.registerPlayerPingEventObserver(this::onPing);
        viewEventProvider.registerPlayerLogoutEventObserver(this::onLogout);

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                onPingTick();
            }

        }, 0, PING_SCHEDULE_MS);
    }

    /**
     * Set the VirtualView's IModelEventProvider
     * The VirtualView will register observers for each model -> view event
     * @param modelEventProvider - The IModelEventProvider
     */
    public void selectModelEventProvider(IModelEventProvider modelEventProvider) {
        modelEventProvider.registerLobbyUpdateEventObserver(this::onLobbyEvent);
        modelEventProvider.registerLobbyRoomUpdateEventObserver(this::onLobbyEvent);
        modelEventProvider.registerLobbyGameStartEventObserver(this::onLobbyEvent);

        modelEventProvider.registerRequestPlayerChallengerSelectGodsEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestPlayerChooseGodEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestPlayerChallengerSelectFirstEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestPlayerEndTurnEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestWorkerBuildBlockEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestWorkerBuildDomeEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestWorkerForceEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestWorkerMoveEventObserver(this::onRequestEvent);
        modelEventProvider.registerRequestWorkerSpawnEventObserver(this::onRequestEvent);

        modelEventProvider.registerPlayerChallengerSelectGodsEventObserver(this::onEvent);
        modelEventProvider.registerPlayerChooseGodEventObserver(this::onEvent);
        modelEventProvider.registerPlayerChallengerSelectFirstEventObserver(this::onEvent);
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

    /**
     * Handle server shutdown
     */
    public void shutdown() {
        timer.cancel();
    }

    /**
     * Check for player timeouts every <code>PING_SCHEDULE_MS</code>
     *
     * A player times out if the last ping received by the server is more than <code>PING_TIMEOUT_MS</code> old
     */
    private void onPingTick() {
        for (Map.Entry<String, Long> lastPlayerPing : lastPlayerPings.entrySet()) {
            String player = lastPlayerPing.getKey();
            long time = lastPlayerPing.getValue();

            if (System.currentTimeMillis() >= time + PING_TIMEOUT_MS) {
                // Player timed out, create a custom PlayerLogoutEvent
                dispatchLogout(player);
                continue;
            }

            server.send(new OutboundMessage(player, serializedEvent(new RequestPlayerPingEvent(player))));
        }
    }

    /**
     * Handles the player initial connection to the server
     * The player is registered in the <code>VirtualView</code> and must reply to ping events to not be disconnected
     *
     * @param tempName The temporary identifier for the player
     */
    private void onConnect(String tempName) {
        lastPlayerPings.put(tempName, System.currentTimeMillis());
    }

    /**
     * Handles a <code>PlayerLoginEvent</code>, checking the player username and age and logging in if they are accepted
     * Players must have a valid username, as defined in the <code>PLAYER_NAME</code> regex
     * No duplicate usernames are allowed
     * The age must be a number greater than 0
     *
     * @param event The player login event
     */
    private void onLogin(PlayerLoginEvent event) {
        if (event.getSender().isEmpty()) {
            return;
        }

        String sender = event.getSender().get();

        if (IServer.isIdentified(sender)) {
            throw new IllegalStateException("Player already logged in");
        }

        String player = event.getPlayer();

        if (!playerNameValidator.isValid(player)) {
            String reason;

            if (player.length() < MIN_NAME_LENGTH) {
                reason = "Must be at least " + MIN_NAME_LENGTH + " characters";
            } else if (player.length() > MAX_NAME_LENGTH) {
                reason = "Can't have more than " + MAX_NAME_LENGTH + " characters";
            } else {
                reason = "Only letters, numbers, underscores and separators are allowed (spaces and dots are allowed inside the name)";
            }

            throw new IllegalArgumentException("Invalid player name (" + reason + ")");
        }

        if (!playerChecker.isNameAvailable(player)) {
            throw new IllegalArgumentException("Another player is already using this name, please use another one");
        }

        if (event.getAge() <= 0) {
            throw new IllegalArgumentException("Your age can't be negative");
        }

        server.identify(sender, player);

        // Replace the temporary identifier with a new one
        lastPlayerPings.remove(sender);
        lastPlayerPings.put(player, System.currentTimeMillis());
    }

    /**
     * Handles a <code>PlayerPingEvent</code>, saving the current ping time that will be used to check for timeouts
     *
     * @param event The player ping event
     */
    private void onPing(PlayerPingEvent event) {
        if (event.getSender().isEmpty()) {
            return;
        }

        // Player replied to a ping event
        lastPlayerPings.put(event.getSender().get(), System.currentTimeMillis());
    }

    /**
     * Handles a <code>PlayerLogoutEvent</code>, disconnecting the player
     *
     * @param event The player logout event
     */
    private void onLogout(PlayerLogoutEvent event) {
        if (!event.isValid()) {
            return;
        }

        // Player wants to disconnect
        disconnect(event.getPlayer());
    }

    /**
     * Handles an <code>InboundMessage</code> received from a player
     * The message must contain a valid event, that will be deserialized and the resulting event forwarded to the
     * appropriate event providers
     * The message will be rejected (with a response event) if there's an error while parsing, if the event is invalid
     * or if the player that sent the message is not the same specified in the event, otherwise the event will be
     * forwarded to the <code>Controller</code>, that will reply accordingly
     *
     * @param message The message received by the server
     */
    private void onMessage(InboundMessage message) {
        AbstractEvent event;

        try {
            event = eventSerializer.deserialize(message.getMessage());
        } catch (SerializationException exception) {
            // Bad client
            Logger.getInstance().warning("Invalid message received: " + exception.getMessage());
            onResponse(new ResponseInvalidEvent(message.getSourcePlayer()));
            return;
        }

        String sender = message.getSourcePlayer();
        event.setSender(sender);

        if (IServer.isIdentified(sender)) {
            if (!event.isValid()) {
                // Bad client
                Logger.getInstance().warning("Invalid player in event from " + event.getSender());
                onResponse(new ResponseInvalidEvent(message.getSourcePlayer()));
                sendInvalidResponse(sender);
                return;
            }
        }

        try {
            event.accept(getViewEventProvider());
        } catch (IllegalStateException exception) {
            // Bad client, didn't send a view -> model event or sent a login after already logging in
            Logger.getInstance().warning("Invalid event received from " + sender + ": " + event.getClass().getSimpleName());
            onResponse(new ResponseInvalidEvent(sender));
        } catch (IllegalArgumentException exception) {
            // ClientConnector sent a login event with invalid parameters
            onResponse(new ResponseInvalidLoginEvent(sender, exception.getMessage()));
        }
    }

    /**
     * Handles an <code>ErrorMessage</code> received from the server while sending a message
     * If there's an error sending a message, the related player will be disconnected
     * (TCP will try to send the packet multiple times before failing and getting to this point)
     *
     * @param message The error message
     */
    private void onError(ErrorMessage message) {
        Optional<OutboundMessage> outboundMessage = message.getOutboundMessage();

        if (outboundMessage.isEmpty()) {
            return;
        }

        // Disconnect the player
        String player = outboundMessage.get().getDestinationPlayer();
        dispatchLogout(player);
    }

    /**
     * Disconnect the player, closing it's connection and removing the player from the list of connected players
     *
     * @param player The player
     */
    private void disconnect(String player) {
        server.disconnect(player);
        lastPlayerPings.remove(player);
    }

    /**
     * Handles a <code>AbstractLobbyEvent</code>
     * The event will be sent to the event player
     *
     * @param event The lobby event
     */
    private void onLobbyEvent(AbstractLobbyEvent event) {
        String serialized = serializedEvent(event);
        dispatchEvent(event.getPlayer(), serialized);
    }

    /**
     * Handles a <code>AbstractRequestEvent</code>
     * The event will be sent to the event player
     *
     * @param event The request event
     */
    private void onRequestEvent(AbstractRequestEvent event) {
        String serialized = serializedEvent(event);
        dispatchEvent(event.getPlayer(), serialized);
    }

    /**
     * Handles a <code>AbstractResponseEvent</code>
     * The event will be sent to the event player
     *
     * @param event The response event
     */
    private void onResponse(AbstractResponseEvent event) {
        String serialized = serializedEvent(event);
        dispatchEvent(event.getPlayer(), serialized);
    }

    /**
     * Handles a generic <code>AbstractEvent</code>
     * The event will be sent to the event receivers
     *
     * @param event The event
     */
    private void onEvent(AbstractEvent event) {
        dispatchEvent(event);
    }

    /**
     * Send an event to every receiving player
     *
     * @param event The event
     */
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

    /**
     * Send a message (a serialized event) to the specified player
     *
     * @param player The player
     * @param serialized The message
     */
    private void dispatchEvent(String player, String serialized) {
        server.send(new OutboundMessage(player, serialized));
    }

    /**
     * Notify the controller that a player has disconnected
     *
     * @param player The player
     */
    private void dispatchLogout(String player) {
        var event = new PlayerLogoutEvent(player);
        event.setSender(player);
        event.accept(getViewEventProvider());
    }

    /**
     * Serialize an event, transforming it to a <code>String</code>
     *
     * @param event The event to be serialized
     * @return The serialized message
     */
    private String serializedEvent(AbstractEvent event) {
        return eventSerializer.serialize(event);
    }

    /**
     * Sends a <code>ResponseInvalidParametersEvent</code> to the player
     *
     * @param player The player
     */
    private void sendInvalidResponse(String player) {
        String serialized = serializedEvent(new ResponseInvalidParametersEvent(player));
        server.send(new OutboundMessage(player, serialized));
    }

}
