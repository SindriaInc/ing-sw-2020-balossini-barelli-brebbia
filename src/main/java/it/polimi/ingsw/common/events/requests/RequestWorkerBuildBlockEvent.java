package it.polimi.ingsw.common.events.requests;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;

public class RequestWorkerBuildBlockEvent extends AbstractRequestWorkerInteractEvent {

    public RequestWorkerBuildBlockEvent(int worker, List<Coordinates> availableDestinations) {
        super(worker, availableDestinations);
    }

}
