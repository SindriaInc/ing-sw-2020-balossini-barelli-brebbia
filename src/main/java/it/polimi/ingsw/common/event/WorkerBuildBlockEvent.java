package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for a worker block built
 *
 * View -> Model
 * Model -> View
 */
public class WorkerBuildBlockEvent extends AbstractWorkerInteractEvent {

    /**
     * Class constructor
     *
     * @param player The owner
     * @param worker The worker id
     * @param destination The block destination
     */
    public WorkerBuildBlockEvent(String player, int worker, Coordinates destination) {
        super(player, worker, destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getWorkerBuildBlockEventObservable().notifyObservers(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getWorkerBuildBlockEventObservable().notifyObservers(this);
    }

}
