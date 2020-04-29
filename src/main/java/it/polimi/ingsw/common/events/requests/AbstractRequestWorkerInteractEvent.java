package it.polimi.ingsw.common.events.requests;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;

public abstract class AbstractRequestWorkerInteractEvent {

    private final int worker;
    private final List<Coordinates> availableDestinations;

    public AbstractRequestWorkerInteractEvent(int worker, List<Coordinates> availableDestinations) {
        this.worker = worker;
        this.availableDestinations = availableDestinations;
    }

    public int getWorker() {
        return worker;
    }

    public List<Coordinates> getAvailableDestinations() {
        return availableDestinations;
    }

}
