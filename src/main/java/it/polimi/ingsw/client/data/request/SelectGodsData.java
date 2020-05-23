package it.polimi.ingsw.client.data.request;

import it.polimi.ingsw.common.info.GodInfo;

import java.util.List;

public class SelectGodsData {

    /**
     * The list of gods available for the selection
     */
    private final List<GodInfo> availableGods;

    /**
     * The number of gods that should be chosen
     */
    private final int selectGodsCount;

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
