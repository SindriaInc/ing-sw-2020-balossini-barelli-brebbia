package it.polimi.ingsw.common;

import java.util.List;

public interface ViewEventListener {

    /**
     * Listener for the god selection from the deck
     */
    ViewEventResponse onSelectGods(String player, List<String> gods);

    /**
     * Listener for the god selection by each player
     */
    ViewEventResponse onChooseGod(String player, String god);

    /**
     * Listener for the worker spawn
     */
    ViewEventResponse onSpawnWorker(String player, Coordinates position);

    /**
     * Listener for the worker movement
     */
    ViewEventResponse onMoveWorker(String player, int worker, Coordinates destination);

    /**
     * Listener for the worker block building
     */
    ViewEventResponse onBuildBlock(String player, int worker, Coordinates destination);

    /**
     * Listener for the worker dome building
     */
    ViewEventResponse onBuildDome(String player, int worker, Coordinates destination);

    /**
     * Listener for the force move
     */
    ViewEventResponse onForceWorker(String player, int worker, int target, Coordinates destination);

    /**
     * Listener for the turn end
     */
    ViewEventResponse onEndTurn(String player);

}
