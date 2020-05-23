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

    private final Coordinates position;

    public WorkerSpawnEvent(String player, int id, Coordinates position) {
        super(player, id);

        this.position = position;
    }

    public Coordinates getPosition() {
        return position;
    }

    @Override
    public void accept(ModelEventProvider provider) {
        provider.getWorkerSpawnEventObservable().notifyObservers(this);
    }

    @Override
    public void accept(ViewEventProvider provider) {
        provider.getWorkerSpawnEventObservable().notifyObservers(this);
    }

}
