package it.polimi.ingsw.common.event.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.ingsw.common.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractRequestWorkerInteractEvent extends AbstractRequestEvent {

    public static final String ATTRIBUTE_WORKER = "worker";
    public static final String ATTRIBUTE_AVAILABLE_DESTINATIONS = "availableDestinations";

    private final int worker;
    private final List<Coordinates> availableDestinations;

    public AbstractRequestWorkerInteractEvent(String player, int worker, List<Coordinates> availableDestinations) {
        super(player);

        this.worker = worker;
        this.availableDestinations = availableDestinations;
    }

    public int getWorker() {
        return worker;
    }

    public List<Coordinates> getAvailableDestinations() {
        return availableDestinations;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();

        JsonArray coordinatesArray = new JsonArray();

        for (Coordinates coordinates : availableDestinations) {
            coordinatesArray.add(coordinates.toString());
        }

        serialized.put(ATTRIBUTE_WORKER, Integer.toString(worker));
        serialized.put(ATTRIBUTE_AVAILABLE_DESTINATIONS, coordinatesArray.toString());
        return serialized;
    }

    public static int deserializeWorker(String worker) {
        return Integer.parseInt(worker);
    }

    public static List<Coordinates> deserializeAvailableDestinations(String raw) {
        List<Coordinates> availableDestinations = new ArrayList<>();

        for (JsonElement coordinates : JsonParser.parseString(raw).getAsJsonArray()) {
            availableDestinations.add(Coordinates.parse(coordinates.getAsString()));
        }

        return availableDestinations;
    }

}
