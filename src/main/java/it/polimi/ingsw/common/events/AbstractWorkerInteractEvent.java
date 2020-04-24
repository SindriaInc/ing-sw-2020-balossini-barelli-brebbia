package it.polimi.ingsw.common.events;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

public abstract class AbstractWorkerInteractEvent extends AbstractWorkerEvent {

    private final Cell cell;

    public AbstractWorkerInteractEvent(Worker worker, Cell cell) {
        super(worker);

        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

}
