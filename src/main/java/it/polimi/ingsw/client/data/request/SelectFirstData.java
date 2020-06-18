package it.polimi.ingsw.client.data.request;

import java.util.List;

public class SelectFirstData {

    /**
     * The list of players in the game that can be chosen as first
     */
    private final List<String> availablePlayers;

    public SelectFirstData(List<String> availablePlayers) {
        this.availablePlayers = List.copyOf(availablePlayers);
    }

    public List<String> getAvailablePlayers() {
        return availablePlayers;
    }

}
