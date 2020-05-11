package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;

public abstract class AbstractRequestWorkerInteractEvent extends AbstractRequestEvent {

    private final int worker;
    private final List<Coordinates> availableDestinations;

    public AbstractRequestWorkerInteractEvent(String player, int worker, List<Coordinates> availableDestinations) {
        super(player);

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
