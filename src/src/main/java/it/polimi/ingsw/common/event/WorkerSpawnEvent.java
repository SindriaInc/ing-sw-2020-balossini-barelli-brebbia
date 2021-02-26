package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for a worker spawn
 *
 * View -> Model
 * Model -> View
 */
public class WorkerSpawnEvent extends AbstractWorkerEvent {

    /**
     * The worker position
     */
    private final Coordinates position;

    /**
     * Class constructor
     *
     * @param player The owner
     * @param id The worker id
     * @param position The worker position
     */
    public WorkerSpawnEvent(String player, int id, Coordinates position) {
        super(player, id);

        this.position = position;
    }

    public Coordinates getPosition() {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getWorkerSpawnEventObservable().notifyObservers(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getWorkerSpawnEventObservable().notifyObservers(this);
    }

}
