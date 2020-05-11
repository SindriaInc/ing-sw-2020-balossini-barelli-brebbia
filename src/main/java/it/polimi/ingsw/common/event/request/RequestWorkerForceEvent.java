package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;
import java.util.Map;

public class RequestWorkerForceEvent extends AbstractRequestEvent {

    private final int worker;

    private final Map<Integer, List<Coordinates>> availableTargetDestinations;

    public RequestWorkerForceEvent(String player, int worker, Map<Integer, List<Coordinates>> availableTargetDestinations) {
        super(player);

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
