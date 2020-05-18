package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for a worker block built
 *
 * View -> Model
 * Model -> View
 */
public class WorkerBuildBlockEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildBlockEvent(String player, int worker, Coordinates destination) {
        super(player, worker, destination);
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getWorkerBuildBlockEventObservable().notifyObservers(this);
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getWorkerBuildBlockEventObservable().notifyObservers(this);
    }

}
