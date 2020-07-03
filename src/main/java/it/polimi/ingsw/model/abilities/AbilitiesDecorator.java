package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.gamestates.AbstractGameState;

import java.util.List;

/**
 * The abstract class implemented by the decorators which apply to the player's workers
 */
public abstract class AbilitiesDecorator implements IAbilities {

    private final IAbilities abilities;

    /**
     * Class constructor
     * @param abilities The abilities to be decorated
     */
    public AbilitiesDecorator(IAbilities abilities) {
        this.abilities = abilities;
    }

    /**
     * @see IAbilities#checkHasWon(Turn)
     */
    @Override
    public boolean checkHasWon(Turn turn) {
        return abilities.checkHasWon(turn);
    }

    /**
     * @see IAbilities#checkCanMove(Turn, Cell)
     */
    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        return abilities.checkCanMove(turn, cell);
    }

    /**
     * @see IAbilities#doMove(Turn, Cell)
     */
    @Override
    public void doMove(Turn turn, Cell cell) {
        abilities.doMove(turn, cell);
    }

    /**
     * @see IAbilities#checkCanBuildBlock(Turn, Cell)
     */
    @Override
    public boolean checkCanBuildBlock(Turn turn, Cell cell) {
        return abilities.checkCanBuildBlock(turn, cell);
    }

    /**
     * @see IAbilities#doBuildBlock(Turn, Cell)
     */
    @Override
    public void doBuildBlock(Turn turn, Cell cell) {
        abilities.doBuildBlock(turn, cell);
    }

    /**
     * @see IAbilities#checkCanBuildDome(Turn, Cell)
     */
    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        return abilities.checkCanBuildDome(turn, cell);
    }

    /**
     * @see IAbilities#doBuildDome(Turn, Cell)
     */
    @Override
    public void doBuildDome(Turn turn, Cell cell) {
        abilities.doBuildDome(turn, cell);
    }

    /**
     * @see IAbilities#checkCanForce(Turn, Worker, Cell)
     */
    @Override
    public boolean checkCanForce(Turn turn, Worker worker, Cell cell) {
        return abilities.checkCanForce(turn, worker, cell);
    }

    /**
     * @see IAbilities#doForce(Turn, Worker, Cell)
     */
    @Override
    public void doForce(Turn turn, Worker worker, Cell cell) {
        abilities.doForce(turn, worker, cell);
    }
}
