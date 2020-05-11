package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;

public class RequestWorkerMoveEvent extends AbstractRequestWorkerInteractEvent {

    public RequestWorkerMoveEvent(String player, int worker, List<Coordinates> availableDestinations) {
        super(player, worker, availableDestinations);
    }

}
