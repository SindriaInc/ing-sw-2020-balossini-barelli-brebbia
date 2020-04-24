package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.ActionEventListener;
import it.polimi.ingsw.common.ActionEventResponse;
import it.polimi.ingsw.model.*;

import java.util.List;

public class GameController implements ActionEventListener {

    private final Game game;

    public GameController(Game game) {
        this.game = game;
    }

    @Override
    public ActionEventResponse onSelectGods(Player player, List<God> gods) {
        if (!checkPlayer(player)) {
            return ActionEventResponse.INVALID_PLAYER;
        }

        return ActionEventResponse.of(game.selectGods(gods));
    }

    @Override
    public ActionEventResponse onChooseGod(Player player, God god) {
        if (!checkPlayer(player)) {
            return ActionEventResponse.INVALID_PLAYER;
        }

        return ActionEventResponse.of(game.chooseGod(god));
    }

    @Override
    public ActionEventResponse onSpawnWorker(Player player, Worker worker) {
        if (!checkPlayer(player)) {
            return ActionEventResponse.INVALID_PLAYER;
        }

        return ActionEventResponse.of(game.spawnWorker(worker));
    }

    @Override
    public ActionEventResponse onMoveWorker(Player player, Worker worker, Cell destination) {
        if (!checkPlayer(player)) {
            return ActionEventResponse.INVALID_PLAYER;
        }

        return ActionEventResponse.of(game.moveWorker(worker, destination));
    }

    @Override
    public ActionEventResponse onBuildBlock(Player player, Worker worker, Cell destination) {
        if (!checkPlayer(player)) {
            return ActionEventResponse.INVALID_PLAYER;
        }

        return ActionEventResponse.of(game.buildBlock(worker, destination));
    }

    @Override
    public ActionEventResponse onBuildDome(Player player, Worker worker, Cell destination) {
        if (!checkPlayer(player)) {
            return ActionEventResponse.INVALID_PLAYER;
        }

        return ActionEventResponse.of(game.buildDome(worker, destination));
    }

    @Override
    public ActionEventResponse onForceWorker(Player player, Worker worker, Worker target, Cell destination) {
        if (!checkPlayer(player)) {
            return ActionEventResponse.INVALID_PLAYER;
        }

        return ActionEventResponse.of(game.forceWorker(worker, target, destination));
    }

    @Override
    public ActionEventResponse onEndTurn(Player player) {
        if (!checkPlayer(player)) {
            return ActionEventResponse.INVALID_PLAYER;
        }

        return ActionEventResponse.of(game.endTurn());
    }

    private boolean checkPlayer(Player player) {
        return game.getCurrentPlayer().equals(player);
    }

}
