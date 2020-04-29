package it.polimi.ingsw.common.events.requests;

import java.util.List;

public class RequestPlayerChooseGodEvent {

    private final List<String> availableGods;

    public RequestPlayerChooseGodEvent(List<String> availableGods) {
        this.availableGods = availableGods;
    }

    public List<String> getAvailableGods() {
        return availableGods;
    }

}
