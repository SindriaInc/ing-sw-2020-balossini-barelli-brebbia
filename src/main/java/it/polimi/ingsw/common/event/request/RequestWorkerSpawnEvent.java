package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a RequestWorkerSpawnEvent from the player
 * The player may send another view event if the related request was sent between this and a previous non-request event
 */
public class RequestWorkerSpawnEvent extends AbstractRequestEvent {

    private final List<Coordinates> availablePositions;

    public RequestWorkerSpawnEvent(String player, List<Coordinates> availablePositions) {
        super(player);

        this.availablePositions = availablePositions;
    }

    public List<Coordinates> getAvailablePositions() {
        return availablePositions;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestWorkerSpawnEventObservable().notifyObservers(this);
    }

}
