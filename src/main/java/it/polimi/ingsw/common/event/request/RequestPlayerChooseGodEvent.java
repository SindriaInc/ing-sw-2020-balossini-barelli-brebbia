package it.polimi.ingsw.common.event.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestPlayerChooseGodEvent extends AbstractRequestEvent {

    public static final String ATTRIBUTE_AVAILABLE_GODS = "availableGods";

    private final List<String> availableGods;

    public RequestPlayerChooseGodEvent(String player, List<String> availableGods) {
        super(player);

        this.availableGods = availableGods;
    }

    public List<String> getAvailableGods() {
        return availableGods;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();

        JsonArray jsonArray = new JsonArray();

        for (String god : availableGods) {
            jsonArray.add(god);
        }

        serialized.put("availableGods", jsonArray.toString());
        return serialized;
    }

    public static RequestPlayerChooseGodEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        List<String> gods = new ArrayList<>();

        for (JsonElement god : JsonParser.parseString(attributes.get(ATTRIBUTE_AVAILABLE_GODS)).getAsJsonArray()) {
            gods.add(god.getAsString());
        }

        return new RequestPlayerChooseGodEvent(player, gods);
    }

}
