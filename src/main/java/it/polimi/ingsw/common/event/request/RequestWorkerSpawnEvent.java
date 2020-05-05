package it.polimi.ingsw.common.event.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.ingsw.common.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestWorkerSpawnEvent extends AbstractRequestEvent {

    public static final String ATTRIBUTE_AVAILABLE_POSITIONS = "availablePositions";

    private final List<Coordinates> availablePositions;

    public RequestWorkerSpawnEvent(String player, List<Coordinates> availablePositions) {
        super(player);

        this.availablePositions = availablePositions;
    }

    public List<Coordinates> getAvailablePositions() {
        return availablePositions;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();

        JsonArray coordinatesArray = new JsonArray();

        for (Coordinates coordinates : availablePositions) {
            coordinatesArray.add(coordinates.toString());
        }

        serialized.put(ATTRIBUTE_AVAILABLE_POSITIONS, coordinatesArray.toString());
        return serialized;
    }

    public static RequestWorkerSpawnEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        List<Coordinates> availableDestinations = new ArrayList<>();

        for (JsonElement coordinates : JsonParser.parseString(attributes.get(ATTRIBUTE_AVAILABLE_POSITIONS)).getAsJsonArray()) {
            availableDestinations.add(Coordinates.parse(coordinates.getAsString()));
        }

        return new RequestWorkerSpawnEvent(player, availableDestinations);
    }

}
