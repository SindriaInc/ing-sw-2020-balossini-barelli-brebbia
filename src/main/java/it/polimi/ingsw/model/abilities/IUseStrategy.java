package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.*;

public interface IUseStrategy {
    /**
     * Check if a worker can interact with a cell in the board when it can interact with workers too
     * @param turn The current turn
     * @param cell The cell
     * @return true if the interaction is allowed
     */
    boolean canInteractWorkersIncluded(Turn turn, Cell cell);

    /**
     * Check if a worker can interact with a cell in the board when it can't interact with workers
     * @param turn The current turn
     * @param cell The cell
     * @return true if the interaction is allowed
     */
    boolean canInteractWorkersNotIncluded(Turn turn, Cell cell);
}
