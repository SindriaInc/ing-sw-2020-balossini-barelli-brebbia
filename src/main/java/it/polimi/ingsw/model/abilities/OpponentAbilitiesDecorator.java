package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.Worker;

import java.util.List;

/**
 * The abstract class implemented by the decorators which apply to opponents workers
 */
public abstract class OpponentAbilitiesDecorator extends AbilitiesDecorator {

    /**
     * The original player workers
     */
    private List<Worker> workers;

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     * @param workers The list of opponent workers
     */
    public OpponentAbilitiesDecorator(IAbilities abilities, List<Worker> workers) {
        super(abilities);

        this.workers = workers;
    }

    /**
     * Obtains a list of workers
     * @return The list of workers
     */
    public List<Worker> getWorkers() {
        return List.copyOf(workers);
    }

}
