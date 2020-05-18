package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for a worker force move
 *
 * View -> Model
 * Model -> View
 */
public class WorkerForceEvent extends AbstractWorkerInteractEvent {

    private final int target;

    public WorkerForceEvent(String player, int worker, int target, Coordinates destination) {
        super(player, worker, destination);

        this.target = target;
    }

    public int getTarget() {
        return target;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getWorkerForceEventObservable().notifyObservers(this);
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getWorkerForceEventObservable().notifyObservers(this);
    }

}
