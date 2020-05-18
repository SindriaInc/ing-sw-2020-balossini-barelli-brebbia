package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for a worker movement
 *
 * View -> Model
 * Model -> View
 */
public class WorkerMoveEvent extends AbstractWorkerInteractEvent {

    public WorkerMoveEvent(String player, int worker, Coordinates coordinates) {
        super(player, worker, coordinates);
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getWorkerMoveEventObservable().notifyObservers(this);
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getWorkerMoveEventObservable().notifyObservers(this);
    }

}
