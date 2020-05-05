package it.polimi.ingsw.view;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.common.IModelEventProvider;
import it.polimi.ingsw.common.IResponseEventProvider;
import it.polimi.ingsw.common.Observable;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.request.*;
import it.polimi.ingsw.common.event.response.*;
import it.polimi.ingsw.server.message.ErrorMessage;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.server.message.InboundMessage;
import it.polimi.ingsw.server.message.OutboundMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtualView {

    private static final String EVENTS_PACKAGE = "it.polimi.ingsw.common.event.";
    private static final String PROPERTY_ATTRIBUTES = "attributes";
    private static final String PROPERTY_EVENT_NAME = "event";

    private final IServer server;

    private final ViewEventProvider viewEventProvider;

    private final List<String> players = new ArrayList<>();

    public VirtualView(IServer server, IResponseEventProvider responseEventProvider) {
        this.server = server;

        server.registerHandler(this::onMessage);
        server.registerHandler(this::onError);

        responseEventProvider.registerResponseInvalidPlayerEventObserver(this::onResponseInvalidPlayer);
        responseEventProvider.registerResponseInvalidParametersEventObserver(this::onResponseInvalidParameters);
        responseEventProvider.registerResponseInvalidStateEventObserver(this::onResponseInvalidState);

        viewEventProvider = new ViewEventProvider();
    }

    public void selectModelEventProvider(IModelEventProvider modelEventProvider) {
        modelEventProvider.registerPlayerRequestChallengerSelectGodsEventObserver(this::onRequestChallengerSelectGods);
        modelEventProvider.registerRequestPlayerChooseGodEventObserver(this::onRequestPlayerChooseGod);
        modelEventProvider.registerRequestPlayerEndTurnEventObserver(this::onRequestPlayerEndTurn);
        modelEventProvider.registerRequestWorkerBuildBlockEventObserver(this::onRequestWorkerBuildBlock);
        modelEventProvider.registerRequestWorkerBuildDomeEventObserver(this::onRequestWorkerBuildDome);
        modelEventProvider.registerRequestWorkerForceEventObserver(this::onRequestWorkerForce);
        modelEventProvider.registerRequestWorkerMoveEventObserver(this::onRequestWorkerMove);
        modelEventProvider.registerRequestWorkerSpawnEventObserver(this::onRequestWorkerSpawn);

        modelEventProvider.registerPlayerChallengerSelectGodsEventObserver(this::onChallengerSelectGods);
        modelEventProvider.registerPlayerChooseGodEventObserver(this::onPlayerChooseGod);
        modelEventProvider.registerPlayerLoseEventObserver(this::onPlayerLose);
        modelEventProvider.registerPlayerTurnStartEventObserver(this::onPlayerTurnStart);
        modelEventProvider.registerPlayerWinEventObserver(this::onPlayerWin);
        modelEventProvider.registerWorkerBuildBlockEventObserver(this::onWorkerBuildBlock);
        modelEventProvider.registerWorkerBuildDomeEventObserver(this::onWorkerBuildDome);
        modelEventProvider.registerWorkerForceEventObserver(this::onWorkerForce);
        modelEventProvider.registerWorkerSpawnEventObserver(this::onWorkerSpawn);
    }

    public ViewEventProvider getViewEventProvider() {
        return viewEventProvider;
    }

    private void onMessage(InboundMessage message) {
        JsonObject jsonObject = JsonParser.parseString(message.getMessage()).getAsJsonObject();

        String eventName = jsonObject.get(PROPERTY_EVENT_NAME).getAsString();
        JsonObject jsonAttributes = jsonObject.get(PROPERTY_ATTRIBUTES).getAsJsonObject();

        Map<String, String> attributes = new HashMap<>();
        for (Map.Entry<String, JsonElement> element : jsonAttributes.entrySet()) {
            attributes.put(element.getKey(), element.getValue().getAsString());
        }

        String classPackage = EVENTS_PACKAGE;

        if (eventName.startsWith("Request")) {
            classPackage += "request.";
        } else if (eventName.startsWith("Response")) {
            classPackage += "response.";
        }

        // The event class
        Class<?> eventClass;

        try {
            eventClass = Class.forName(classPackage + eventName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            dispatchResponseEvent(new ResponseInvalidEvent(message.getSourcePlayer()));
            return;
        }

        // Obtain the static method that deserializes the event
        Method deserializeAttributes;

        try {
            deserializeAttributes = eventClass.getDeclaredMethod("deserializeAttributes", Map.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            dispatchResponseEvent(new ResponseInvalidEvent(message.getSourcePlayer()));
            return;
        }

        // The deserialized event
        AbstractEvent event;

        try {
            event = (AbstractEvent) deserializeAttributes.invoke(null, attributes);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            dispatchResponseEvent(new ResponseInvalidEvent(message.getSourcePlayer()));
            return;
        }

        handleEvent(message.getSourcePlayer(), eventName, event);
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
        } catch (NoSuchMethodException e) {
            // Should never get here unless ViewEventProvider is badly implemented
            e.printStackTrace();
            dispatchResponseEvent(new ResponseInvalidEvent(player));
            return;
        }

        // The observable related to the event
        Observable<AbstractEvent> observable;

        try {
            observable = (Observable<AbstractEvent>) observableMethod.invoke(viewEventProvider);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // Should never get here since we're calling a getter
            e.printStackTrace();
            dispatchResponseEvent(new ResponseInvalidEvent(player));
            return;
        }

        observable.notifyObservers(event);
    }

    private void onRequestChallengerSelectGods(RequestPlayerChallengerSelectGodsEvent event) {
        dispatchRequestEvent(event);
    }

    private void onRequestPlayerChooseGod(RequestPlayerChooseGodEvent event) {
        dispatchRequestEvent(event);
    }

    private void onRequestPlayerEndTurn(RequestPlayerEndTurnEvent event) {
        dispatchRequestEvent(event);
    }

    private void onRequestWorkerBuildBlock(RequestWorkerBuildBlockEvent event) {
        dispatchRequestEvent(event);
    }

    private void onRequestWorkerBuildDome(RequestWorkerBuildDomeEvent event) {
        dispatchRequestEvent(event);
    }

    private void onRequestWorkerForce(RequestWorkerForceEvent event) {
        dispatchRequestEvent(event);
    }

    private void onRequestWorkerMove(RequestWorkerMoveEvent event) {
        dispatchRequestEvent(event);
    }

    private void onRequestWorkerSpawn(RequestWorkerSpawnEvent event) {
        dispatchRequestEvent(event);
    }

    private void onPlayerTurnStart(PlayerTurnStartEvent event) {
        dispatchEvent(event);
    }

    private void onChallengerSelectGods(PlayerChallengerSelectGodsEvent event) {
        dispatchEvent(event);
    }

    private void onPlayerChooseGod(PlayerChooseGodEvent event) {
        dispatchEvent(event);
    }

    private void onPlayerLose(PlayerLoseEvent event) {
        dispatchEvent(event);
    }

    private void onPlayerWin(PlayerWinEvent event) {
        dispatchEvent(event);
    }

    private void onWorkerBuildBlock(WorkerBuildBlockEvent event) {
        dispatchEvent(event);
    }

    private void onWorkerBuildDome(WorkerBuildDomeEvent event) {
        dispatchEvent(event);
    }

    private void onWorkerForce(WorkerForceEvent event) {
        dispatchEvent(event);
    }

    private void onWorkerSpawn(WorkerSpawnEvent event) {
        dispatchEvent(event);
    }

    private void onResponseInvalidPlayer(ResponseInvalidPlayerEvent event) {
        dispatchResponseEvent(event);
    }

    private void onResponseInvalidParameters(ResponseInvalidParametersEvent event) {
        dispatchResponseEvent(event);
    }

    private void onResponseInvalidState(ResponseInvalidStateEvent event) {
        dispatchResponseEvent(event);
    }

    private void dispatchResponseEvent(AbstractResponseEvent event) {
        String serialized = serializedEvent(event);

        dispatchEvent(event.getPlayer(), serialized);
    }

    private void dispatchRequestEvent(AbstractRequestEvent event) {
        String serialized = serializedEvent(event);

        dispatchEvent(event.getPlayer(), serialized);
    }

    private void dispatchEvent(AbstractEvent event) {
        String serialized = serializedEvent(event);

        for (String player : players) {
            dispatchEvent(player, serialized);
        }
    }

    private void dispatchEvent(String player, String serialized) {
        server.send(new OutboundMessage(player, serialized));
    }

    private String serializedEvent(AbstractEvent event) {
        JsonObject object = new JsonObject();

        JsonObject attributes = new JsonObject();
        for (Map.Entry<String, String> entry : event.serializeAttributes().entrySet()) {
            attributes.addProperty(entry.getKey(), entry.getValue());
        }

        object.add(PROPERTY_ATTRIBUTES, attributes);
        object.addProperty(PROPERTY_EVENT_NAME, event.getClass().getSimpleName());
        return object.toString();
    }

}
