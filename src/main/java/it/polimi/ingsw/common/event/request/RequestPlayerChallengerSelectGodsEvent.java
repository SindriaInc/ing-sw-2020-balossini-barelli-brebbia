package it.polimi.ingsw.common.event.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestPlayerChallengerSelectGodsEvent extends AbstractRequestEvent {

    public static final String ATTRIBUTE_GODS = "gods";
    public static final String ATTRIBUTE_SELECTED_GODS_COUNT = "selectedGodsCount";

    private final List<String> gods;

    private final int selectedGodsCount;

    public RequestPlayerChallengerSelectGodsEvent(String player, List<String> gods, int selectedGodsCount) {
        super(player);

        this.gods = gods;
        this.selectedGodsCount = selectedGodsCount;
    }

    public List<String> getGods() {
        return gods;
    }

    public int getSelectedGodsCount() {
        return selectedGodsCount;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();

        JsonArray jsonArray = new JsonArray();

        for (String god : gods) {
            jsonArray.add(god);
        }

        serialized.put(ATTRIBUTE_GODS, jsonArray.toString());
        serialized.put(ATTRIBUTE_SELECTED_GODS_COUNT, Integer.toString(selectedGodsCount));
        return serialized;
    }

    public static RequestPlayerChallengerSelectGodsEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        int selectedGodsCount = Integer.parseInt(ATTRIBUTE_SELECTED_GODS_COUNT);
        List<String> gods = new ArrayList<>();

        for (JsonElement god : JsonParser.parseString(attributes.get(ATTRIBUTE_GODS)).getAsJsonArray()) {
            gods.add(god.getAsString());
        }

        return new RequestPlayerChallengerSelectGodsEvent(player, gods, selectedGodsCount);
    }

}
