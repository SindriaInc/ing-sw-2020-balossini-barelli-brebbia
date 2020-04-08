package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.*;

import java.util.List;

public abstract class AbilitiesDecorator implements IAbilities {

    private IAbilities abilities;

    public AbilitiesDecorator(IAbilities abilities) {
        this.abilities = abilities;
    }

    @Override
    public boolean checkHasWon(List<Worker> workers) {
        return abilities.checkHasWon(workers);
    }

    @Override
    public boolean checkCanMove(Turn turn, Cell cell) {
        return abilities.checkCanMove(turn, cell);
    }

    @Override
    public void doMove(Turn turn, Cell cell) {
        abilities.doMove(turn, cell);
    }

    @Override
    public boolean checkCanBuildBlock(Turn turn, Cell cell) {
        return abilities.checkCanBuildBlock(turn, cell);
    }

    @Override
    public void doBuildBlock(Turn turn, Cell cell) {
        abilities.doBuildBlock(turn, cell);
    }

    @Override
    public boolean checkCanBuildDome(Turn turn, Cell cell) {
        return abilities.checkCanBuildDome(turn, cell);
    }

    @Override
    public void doBuildDome(Turn turn, Cell cell) {
        abilities.doBuildDome(turn, cell);
    }

//    @Override
//    public boolean checkCanForce(Turn turn) {
//        return abilities.checkCanForce(turn);
//    }
//
//    @Override
//    public void doForce(Turn turn, Cell cell) {
//        abilities.doForce(turn, cell);
//    }
}
