package it.polimi.ingsw.common.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Event for the god selection by the challenger
 *
 * View -> Model
 * Model -> View
 */
public class PlayerChallengerSelectGodsEvent extends AbstractPlayerEvent {

    public static final String ATTRIBUTE_GODS = "gods";

    private final List<String> gods;

    public PlayerChallengerSelectGodsEvent(String player, List<String> gods) {
        super(player);

        this.gods = gods;
    }

    public List<String> getGods() {
        return gods;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();

        JsonArray jsonArray = new JsonArray();

        for (String god : gods) {
            jsonArray.add(god);
        }

        serialized.put(ATTRIBUTE_GODS, jsonArray.toString());
        return serialized;
    }

    public static PlayerChallengerSelectGodsEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        List<String> gods = new ArrayList<>();

        for (JsonElement god : JsonParser.parseString(attributes.get(ATTRIBUTE_GODS)).getAsJsonArray()) {
            gods.add(god.getAsString());
        }

        return new PlayerChallengerSelectGodsEvent(player, gods);
    }

}
