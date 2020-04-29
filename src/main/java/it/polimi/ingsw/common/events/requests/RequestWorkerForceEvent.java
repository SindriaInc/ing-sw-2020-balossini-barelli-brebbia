package it.polimi.ingsw.common.events.requests;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;
import java.util.Map;

public class RequestWorkerForceEvent {

    private final int worker;

    private final Map<Integer, List<Coordinates>> availableTargetDestinations;

    public RequestWorkerForceEvent(int worker, Map<Integer, List<Coordinates>> availableTargetDestinations) {
        this.worker = worker;
        this.availableTargetDestinations = availableTargetDestinations;
    }

    public int getWorker() {
        return worker;
    }

    public Map<Integer, List<Coordinates>> getAvailableTargetDestinations() {
        return availableTargetDestinations;
    }

}
