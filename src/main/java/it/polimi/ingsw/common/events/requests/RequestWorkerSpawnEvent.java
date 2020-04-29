package it.polimi.ingsw.common.events.requests;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;

public class RequestWorkerSpawnEvent {

    private final List<Coordinates> availablePositions;

    public RequestWorkerSpawnEvent(List<Coordinates> availablePositions) {
        this.availablePositions = availablePositions;
    }

    public List<Coordinates> getAvailablePositions() {
        return availablePositions;
    }

}
