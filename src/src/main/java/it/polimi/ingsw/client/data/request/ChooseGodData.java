package it.polimi.ingsw.client.data.request;

import it.polimi.ingsw.common.info.GodInfo;

import java.util.List;

/**
 * Represent the data of a god choosing
 */
public class ChooseGodData {

    /**
     * The list of gods available for the selection
     */
    private final List<GodInfo> availableGods;

    /**
     * Class constructor, set the available gods
     *
     * @param availableGods The available gods
     */
    public ChooseGodData(List<GodInfo> availableGods) {
        this.availableGods = List.copyOf(availableGods);
    }

    public List<GodInfo> getAvailableGods() {
        return availableGods;
    }

}
