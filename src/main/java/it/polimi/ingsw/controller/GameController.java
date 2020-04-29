package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.ViewEventListener;
import it.polimi.ingsw.common.ViewEventResponse;
import it.polimi.ingsw.model.*;

import java.util.List;

public class GameController implements ViewEventListener {

    /**
     * The model of the game
     */
    private final Game game;

    public GameController(Game game) {
        this.game = game;
    }

    @Override
    public ViewEventResponse onSelectGods(String player, List<String> gods) {
        if (!checkPlayer(player)) {
            return ViewEventResponse.INVALID_PLAYER;
        }

        return ViewEventResponse.of(game.selectGods(gods));
    }

    @Override
    public ViewEventResponse onChooseGod(String player, String god) {
        if (!checkPlayer(player)) {
            return ViewEventResponse.INVALID_PLAYER;
        }

        return ViewEventResponse.of(game.chooseGod(god));
    }

    @Override
    public ViewEventResponse onSpawnWorker(String player, Coordinates position) {
        if (!checkPlayer(player)) {
            return ViewEventResponse.INVALID_PLAYER;
        }

        return ViewEventResponse.of(game.spawnWorker(position));
    }

    @Override
    public ViewEventResponse onMoveWorker(String player, int worker, Coordinates destination) {
        if (!checkPlayer(player)) {
            return ViewEventResponse.INVALID_PLAYER;
        }

        return ViewEventResponse.of(game.moveWorker(worker, destination));
    }

    @Override
    public ViewEventResponse onBuildBlock(String player, int worker, Coordinates destination) {
        if (!checkPlayer(player)) {
            return ViewEventResponse.INVALID_PLAYER;
        }

        return ViewEventResponse.of(game.buildBlock(worker, destination));
    }

    @Override
    public ViewEventResponse onBuildDome(String player, int worker, Coordinates destination) {
        if (!checkPlayer(player)) {
            return ViewEventResponse.INVALID_PLAYER;
        }

        return ViewEventResponse.of(game.buildDome(worker, destination));
    }

    @Override
    public ViewEventResponse onForceWorker(String player, int worker, int target, Coordinates destination) {
        if (!checkPlayer(player)) {
            return ViewEventResponse.INVALID_PLAYER;
        }

        return ViewEventResponse.of(game.forceWorker(worker, target, destination));
    }

    @Override
    public ViewEventResponse onEndTurn(String player) {
        if (!checkPlayer(player)) {
            return ViewEventResponse.INVALID_PLAYER;
        }

        return ViewEventResponse.of(game.endTurn());
    }

    private boolean checkPlayer(String player) {
        return game.getCurrentPlayer().getName().equals(player);
    }

}
