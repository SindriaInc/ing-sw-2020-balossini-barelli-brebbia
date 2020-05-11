package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;

public class RequestWorkerSpawnEvent extends AbstractRequestEvent {

    private final List<Coordinates> availablePositions;

    public RequestWorkerSpawnEvent(String player, List<Coordinates> availablePositions) {
        super(player);

        this.availablePositions = availablePositions;
    }

    public List<Coordinates> getAvailablePositions() {
        return availablePositions;
    }

}
