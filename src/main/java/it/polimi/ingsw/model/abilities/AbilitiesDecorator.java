package it.polimi.ingsw.model.abilities;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

public abstract class AbilitiesDecorator implements IAbilities {

    private IAbilities abilities;

    public AbilitiesDecorator(IAbilities abilities) {
        this.abilities = abilities;
    }

    @Override
    public boolean checkHasWon() {
        return abilities.checkHasWon();
    }

    @Override
    public boolean checkCanMove(Worker worker, Cell cell) {
        return abilities.checkCanMove(worker, cell);
    }

    @Override
    public void doMove(Worker worker, Cell cell) {
        abilities.doMove(worker, cell);
    }

    @Override
    public boolean checkCanBuildBlock(Worker worker, Cell cell) {
        return abilities.checkCanBuildBlock(worker, cell);
    }

    @Override
    public void doBuildBlock(Worker worker, Cell cell) {
        abilities.doBuildBlock(worker, cell);
    }

    @Override
    public boolean checkCanBuildDome(Worker worker, Cell cell) {
        return abilities.checkCanBuildDome(worker, cell);
    }

    @Override
    public void doBuildDome(Worker worker, Cell cell) {
        abilities.doBuildDome(worker, cell);
    }

    @Override
    public Board getBoard() {
        return abilities.getBoard();
    }

    @Override
    public Player getPlayer() {
        return abilities.getPlayer();
    }

}
