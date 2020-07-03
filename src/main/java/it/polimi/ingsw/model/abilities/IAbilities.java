package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

/**
 *Interface containing the checks and the actions that con be modified by an ability
 */
public interface IAbilities {

    /**
     * Checks if the workers at the end of the turn meet any win condition
     * @param turn The Turn
     * @return true if any of the workers (of the turn player) met any win conditions
     */
    boolean checkHasWon(Turn turn);

    /**
     * Whether or not the worker selected in the turn can move into the specified cell
     * @param turn The current Turn
     * @param cell The Cell
     * @return true if the action is allowed
     */
    boolean checkCanMove(Turn turn, Cell cell);

    /**
     * Move the worker in the specified cell
     * @param turn The current Turn
     * @param cell The Cell
     */
    void doMove(Turn turn, Cell cell);

    /**
     * Whether or not the worker can build a block in the specified cell
     * @param turn The current Turn
     * @param cell The Cell
     * @return true if the action is allowed
     */
    boolean checkCanBuildBlock(Turn turn, Cell cell);

    /**
     * Builds a block in the specified cell using the worker
     * @param turn The current Turn
     * @param cell The Cell
     */
    void doBuildBlock(Turn turn, Cell cell);

    /**
     * Whether or not the worker can build a dome in the specified cell
     * @param turn The current Turn
     * @param cell The Cell
     * @return true if the action is allowed
     */
    boolean checkCanBuildDome(Turn turn, Cell cell);

    /**
     * Builds a dome in the specified cell using the worker
     * @param turn The current Turn
     * @param cell The Cell
     */
    void doBuildDome(Turn turn, Cell cell);

    /**
     * Whether or not a worker can force another out of his cell
     * @param turn The current Turn
     * @param worker The worker
     * @param cell The cell
     * @return true if the action is allowed
     */
    boolean checkCanForce(Turn turn, Worker worker, Cell cell);

    /**
     * Force a worker from his cell to another
     * @param turn The current Turn
     * @param worker The worker
     * @param cell The cell
     */
    void doForce(Turn turn, Worker worker, Cell cell);
}
