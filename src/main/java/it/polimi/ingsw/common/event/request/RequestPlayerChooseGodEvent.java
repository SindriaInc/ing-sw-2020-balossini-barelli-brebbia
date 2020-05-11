package it.polimi.ingsw.common.event.request;

import java.util.List;

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

}
