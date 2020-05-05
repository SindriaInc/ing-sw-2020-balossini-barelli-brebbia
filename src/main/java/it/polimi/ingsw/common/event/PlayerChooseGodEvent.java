package it.polimi.ingsw.common.event;

import java.util.Map;

/**
 * Event for the god choice by the player
 *
 * View -> Model
 * Model -> View
 */
public class PlayerChooseGodEvent extends AbstractPlayerEvent {

    public static final String ATTRIBUTE_GOD = "god";

    private final String god;

    public PlayerChooseGodEvent(String player, String god) {
        super(player);

        this.god = god;
    }

    public String getGod() {
        return god;
    }

    @Override
    public Map<String, String> serializeAttributes() {
        var serialized = super.serializeAttributes();
        serialized.put(ATTRIBUTE_GOD, god);
        return serialized;
    }

    public static PlayerChooseGodEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        String god = attributes.get(ATTRIBUTE_GOD);
        return new PlayerChooseGodEvent(player, god);
    }

}