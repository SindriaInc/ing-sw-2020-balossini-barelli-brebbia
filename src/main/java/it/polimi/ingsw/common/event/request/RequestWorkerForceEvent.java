package it.polimi.ingsw.common.event.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.common.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.common.event.request.AbstractRequestWorkerInteractEvent.ATTRIBUTE_WORKER;
import static it.polimi.ingsw.common.event.request.AbstractRequestWorkerInteractEvent.deserializeWorker;

public class RequestWorkerForceEvent extends AbstractRequestEvent {

    public static final String ATTRIBUTE_AVAILABLE_TARGET_DESTINATIONS = "availableTargetDestinations";

    private final int worker;

    private final Map<Integer, List<Coordinates>> availableTargetDestinations;

    public RequestWorkerForceEvent(String player, int worker, Map<Integer, List<Coordinates>> availableTargetDestinations) {
        super(player);

        this.worker = worker;
        this.availableTargetDestinations = availableTargetDestinations;
    }

    public int getWorker() {
        return worker;
    }

    public Map<Integer, List<Coordinates>> getAvailableTargetDestinations() {
        return availableTargetDestinations;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();

        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<Integer, List<Coordinates>> availableTargetDestinations : this.availableTargetDestinations.entrySet()) {
            JsonArray coordinatesArray = new JsonArray();

            for (Coordinates coordinates : availableTargetDestinations.getValue()) {
                coordinatesArray.add(coordinates.toString());
            }

            jsonObject.add(availableTargetDestinations.getKey().toString(), coordinatesArray);
        }

        serialized.put(ATTRIBUTE_WORKER, Integer.toString(worker));
        serialized.put(ATTRIBUTE_AVAILABLE_TARGET_DESTINATIONS, jsonObject.toString());
        return serialized;
    }

    public static RequestWorkerForceEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        int worker = deserializeWorker(attributes.get(ATTRIBUTE_WORKER));
        Map<Integer, List<Coordinates>> availableTargetDestinations = new HashMap<>();

        JsonObject jsonObject = JsonParser.parseString(attributes.get(ATTRIBUTE_AVAILABLE_TARGET_DESTINATIONS)).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            int target = deserializeWorker(entry.getKey());

            List<Coordinates> coordinates = new ArrayList<>();

            JsonArray jsonArray = entry.getValue().getAsJsonArray();
            for (JsonElement element : jsonArray) {
                coordinates.add(Coordinates.parse(element.getAsString()));
            }

            availableTargetDestinations.put(target, coordinates);
        }

        return new RequestWorkerForceEvent(player, worker, availableTargetDestinations);
    }

}
