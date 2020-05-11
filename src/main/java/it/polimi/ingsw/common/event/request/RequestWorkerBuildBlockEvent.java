package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;

public class RequestWorkerBuildBlockEvent extends AbstractRequestWorkerInteractEvent {

    public RequestWorkerBuildBlockEvent(String player, int worker, List<Coordinates> availableDestinations) {
        super(player, worker, availableDestinations);
    }

}
