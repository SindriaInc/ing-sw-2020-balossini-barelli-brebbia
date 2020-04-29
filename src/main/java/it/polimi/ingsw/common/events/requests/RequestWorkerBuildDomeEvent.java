package it.polimi.ingsw.common.events.requests;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;

public class RequestWorkerBuildDomeEvent extends AbstractRequestWorkerInteractEvent {

    public RequestWorkerBuildDomeEvent(int worker, List<Coordinates> availableDestinations) {
        super(worker, availableDestinations);
    }
    
}
