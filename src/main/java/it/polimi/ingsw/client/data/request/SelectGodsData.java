package it.polimi.ingsw.client.data.request;

import it.polimi.ingsw.common.info.GodInfo;

import java.util.List;

/**
 * Represent the data of a gods selection
 */
public class SelectGodsData {

    /**
     * The list of gods available for the selection
     */
    private final List<GodInfo> availableGods;

    /**
     * The number of gods that should be chosen
     */
    private final int selectGodsCount;

    /**
     * Class constructor, set the available gods and how many to select
     *
     * @param availableGods The available gods
     * @param selectGodsCount How many gods to select
     */
    public SelectGodsData(List<GodInfo> availableGods, int selectGodsCount) {
        this.availableGods = List.copyOf(availableGods);
        this.selectGodsCount = selectGodsCount;
    }

    public List<GodInfo> getAvailableGods() {
        return availableGods;
    }

    public int getSelectGodsCount() {
        return selectGodsCount;
    }

}
