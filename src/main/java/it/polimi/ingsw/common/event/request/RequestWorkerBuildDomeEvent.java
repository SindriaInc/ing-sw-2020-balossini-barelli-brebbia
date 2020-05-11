package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;

public class RequestWorkerBuildDomeEvent extends AbstractRequestWorkerInteractEvent {

    public RequestWorkerBuildDomeEvent(String player, int worker, List<Coordinates> availableDestinations) {
        super(player, worker, availableDestinations);
    }
    
}
