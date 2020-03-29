package it.polimi.ingsw.model.abilities.decorators;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.abilities.AbilitiesDecorator;
import it.polimi.ingsw.model.abilities.IAbilities;

public class BlockOnPlayerMoveUp extends AbilitiesDecorator {

    /**
     * The player that needs to be checked to see whether or not we need to block the movement
     * Should normally be different than getPlayer()
     */
    private Player playerToCheck;

    public BlockOnPlayerMoveUp(IAbilities abilities, Player playerToCheck) {
        super(abilities);

        this.playerToCheck = playerToCheck;
    }

    @Override
    public boolean checkCanMove(Worker worker, Cell cell) {
        boolean check = false;
        // TODO: Implement additional check
        return check && super.checkCanMove(worker, cell);
    }

    @Override
    public void doMove(Worker worker, Cell cell) {
        super.doMove(worker, cell);
        // TODO: Implement additional effects
    }

}
