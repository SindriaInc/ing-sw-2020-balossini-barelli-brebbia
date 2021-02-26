package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for a worker force move
 *
 * View -> Model
 * Model -> View
 */
public class WorkerForceEvent extends AbstractWorkerInteractEvent {

    /**
     * The worker id of the target to be forced
     */
    private final int target;

    /**
     * Class constructor
     *
     * @param player The owner
     * @param worker The worker id
     * @param target The target worker id
     * @param destination The target destination
     */
    public WorkerForceEvent(String player, int worker, int target, Coordinates destination) {
        super(player, worker, destination);

        this.target = target;
    }

    public int getTarget() {
        return target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ModelEventProvider provider) {
        provider.getWorkerForceEventObservable().notifyObservers(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(ViewEventProvider provider) {
        provider.getWorkerForceEventObservable().notifyObservers(this);
    }

}
