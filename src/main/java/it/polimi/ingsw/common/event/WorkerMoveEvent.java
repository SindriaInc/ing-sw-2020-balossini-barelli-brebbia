package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for a worker movement
 *
 * View -> Model
 * Model -> View
 */
public class WorkerMoveEvent extends AbstractWorkerInteractEvent {

    /**
     * Class constructor
     *
     * @param player The owner
     * @param worker The worker id
     * @param coordinates The new position
     */
    public WorkerMoveEvent(String player, int worker, Coordinates coordinates) {
        super(player, worker, coordinates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getWorkerMoveEventObservable().notifyObservers(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getWorkerMoveEventObservable().notifyObservers(this);
    }

}
