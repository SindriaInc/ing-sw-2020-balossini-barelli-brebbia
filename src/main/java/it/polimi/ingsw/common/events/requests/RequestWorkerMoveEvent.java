package it.polimi.ingsw.common.events.requests;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;

public class RequestWorkerMoveEvent extends AbstractRequestWorkerInteractEvent {

    public RequestWorkerMoveEvent(int worker, List<Coordinates> availableDestinations) {
        super(worker, availableDestinations);
    }
    
}
