package it.polimi.ingsw.common.event.request;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;

import java.util.List;

/**
 * Request a RequestWorkerSpawnEvent from the player
 * The player may send another view event if the related request was sent between this and a previous non-request event
 */
public class RequestWorkerSpawnEvent extends AbstractRequestEvent {

    /**
     * The list of available coordinates where a worker can spawn
     */
    private final List<Coordinates> availablePositions;

    /**
     * Class constructor
     *
     * @param player The player that the request is targeted to
     * @param availablePositions The list of available coordinates
     */
    public RequestWorkerSpawnEvent(String player, List<Coordinates> availablePositions) {
        super(player);

        this.availablePositions = availablePositions;
    }

    /**
     * A copy of the list of available positions
     *
     * @return The list
     */
    public List<Coordinates> getAvailablePositions() {
        return List.copyOf(availablePositions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getRequestWorkerSpawnEventObservable().notifyObservers(this);
    }

}
