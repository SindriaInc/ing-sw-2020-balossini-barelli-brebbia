package it.polimi.ingsw.client.data.request;

import it.polimi.ingsw.common.info.GodInfo;

import java.util.List;

public class ChooseGodData {

    /**
     * The list of gods available for the selection
     */
    private final List<GodInfo> availableGods;

    public ChooseGodData(List<GodInfo> availableGods) {
        this.availableGods = List.copyOf(availableGods);
    }

    public List<GodInfo> getAvailableGods() {
        return availableGods;
    }

}
