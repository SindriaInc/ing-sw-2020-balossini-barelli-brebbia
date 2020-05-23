package it.polimi.ingsw.client.data.request;

import it.polimi.ingsw.common.info.Coordinates;

import java.util.List;

public class InteractData {

    /**
     * The list of coordinates that can be interacted with
     */
    private final List<Coordinates> availableCoordinates;

    public InteractData(List<Coordinates> availableCoordinates) {
        this.availableCoordinates = List.copyOf(availableCoordinates);
    }

    public List<Coordinates> getAvailableCoordinates() {
        return availableCoordinates;
    }

}
