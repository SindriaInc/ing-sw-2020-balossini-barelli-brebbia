package it.polimi.ingsw.common;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

import java.util.List;

public interface ActionEventListener {

    /**
     * Listener for the god selection from the deck
     */
    ActionEventResponse onSelectGods(Player player, List<God> gods);

    /**
     * Listener for the god selection by each player
     */
    ActionEventResponse onChooseGod(Player player, God god);

    /**
     * Listener for the worker spawn
     */
    ActionEventResponse onSpawnWorker(Player player, Worker worker);

    /**
     * Listener for the worker movement
     */
    ActionEventResponse onMoveWorker(Player player, Worker worker, Cell destination);

    /**
     * Listener for the worker block building
     */
    ActionEventResponse onBuildBlock(Player player, Worker worker, Cell destination);

    /**
     * Listener for the worker dome building
     */
    ActionEventResponse onBuildDome(Player player, Worker worker, Cell destination);

    /**
     * Listener for the force move
     */
    ActionEventResponse onForceWorker(Player player, Worker worker, Worker target, Cell destination);

    /**
     * Listener for the turn end
     */
    ActionEventResponse onEndTurn(Player player);

}
