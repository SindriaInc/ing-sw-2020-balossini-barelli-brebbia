package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.Worker;

import java.util.List;

public abstract class OpponentAbilitiesDecorator extends AbilitiesDecorator {

    /**
     * The original player workers
     */
    private List<Worker> workers;

    public OpponentAbilitiesDecorator(IAbilities abilities, List<Worker> workers) {
        super(abilities);

        this.workers = workers;
    }

    public List<Worker> getWorkers() {
        return List.copyOf(workers);
    }

}
