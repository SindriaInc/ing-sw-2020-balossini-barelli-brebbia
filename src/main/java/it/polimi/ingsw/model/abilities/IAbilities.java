package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

public interface IAbilities {

    /**
     * Checks if the associated player meets any win condition
     * @return true if any of the player win conditions is met
     */
    boolean checkHasWon();

    /**
     * Whether or not the worker can move into the specified cell
     * @param worker The Worker to be moved
     * @param cell The Cell
     * @return true if the action is allowed
     */
    boolean checkCanMove(Worker worker, Cell cell);

    /**
     * Move the worker in the specified cell
     * @param worker The Worker to be moved
     * @param cell The Cell
     */
    void doMove(Worker worker, Cell cell);

    /**
     * Whether or not the worker can build a block in the specified cell
     * @param worker The Worker to be used
     * @param cell The Cell
     * @return true if the action is allowed
     */
    boolean checkCanBuildBlock(Worker worker, Cell cell);

    /**
     * Builds a block in the specified cell using the worker
     * @param worker The Worker to be used
     * @param cell The Cell
     */
    void doBuildBlock(Worker worker, Cell cell);

    /**
     * Whether or not the worker can build a dome in the specified cell
     * @param worker The Worker to be used
     * @param cell The Cell
     * @return true if the action is allowed
     */
    boolean checkCanBuildDome(Worker worker, Cell cell);

    /**
     * Builds a dome in the specified cell using the worker
     * @param worker The Worker to be used
     * @param cell The Cell
     */
    void doBuildDome(Worker worker, Cell cell);

    /**
     * Returns the associated board
     * @return The Board
     */
    Board getBoard();

    /**
     * Returns the associated player
     * @return The Player
     */
    Player getPlayer();

}
