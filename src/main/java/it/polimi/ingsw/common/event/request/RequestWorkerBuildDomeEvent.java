package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.Coordinates;

import java.util.List;
import java.util.Map;

public class RequestWorkerBuildDomeEvent extends AbstractRequestWorkerInteractEvent {

    public RequestWorkerBuildDomeEvent(String player, int worker, List<Coordinates> availableDestinations) {
        super(player, worker, availableDestinations);
    }

    public static RequestWorkerBuildDomeEvent deserializeAttributes(Map<String, String> attributes) {
        String player = attributes.get(ATTRIBUTE_PLAYER);
        int worker = deserializeWorker(attributes.get(ATTRIBUTE_WORKER));
        List<Coordinates> availableDestinations = deserializeAvailableDestinations(attributes.get(ATTRIBUTE_AVAILABLE_DESTINATIONS));

        return new RequestWorkerBuildDomeEvent(player, worker, availableDestinations);
    }
    
}
