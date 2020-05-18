package it.polimi.ingsw.common.event;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.model.ModelEventProvider;
import it.polimi.ingsw.view.ViewEventProvider;

/**
 * Event for a worker dome built
 *
 * View -> Model
 * Model -> View
 */
public class WorkerBuildDomeEvent extends AbstractWorkerInteractEvent {

    public WorkerBuildDomeEvent(String player, int worker, Coordinates destination) {
        super(player, worker, destination);
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getWorkerBuildDomeEventObservable().notifyObservers(this);
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getWorkerBuildDomeEventObservable().notifyObservers(this);
    }

}
