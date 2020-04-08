package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.*;

import java.util.List;

public interface IAbilities {

    /**
     * Checks if the workers meet any win condition
     * @param workers The Worker
     * @return true if any of the workers met any win conditions
     */
    boolean checkHasWon(List<Worker> workers);

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
     * @return true if the action is allowed
     */
    boolean checkCanForce(Turn turn, Worker worker, Cell cell);

    /**
     * Force a worker from his cell to another
     * @param turn The current Turn
     * @param cell The Cell
     */
    void doForce(Turn turn, Worker worker, Cell cell);
}
