package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for a worker dome built
 *
 * View -> Model
 * Model -> View
 */
public class WorkerBuildDomeEvent extends AbstractWorkerInteractEvent {

    /**
     * Class constructor
     *
     * @param player The owner
     * @param worker The worker id
     * @param destination The dome destination
     */
    public WorkerBuildDomeEvent(String player, int worker, Coordinates destination) {
        super(player, worker, destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getWorkerBuildDomeEventObservable().notifyObservers(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getWorkerBuildDomeEventObservable().notifyObservers(this);
    }

}
