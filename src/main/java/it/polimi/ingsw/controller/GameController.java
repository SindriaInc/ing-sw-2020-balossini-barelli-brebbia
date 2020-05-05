package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.IViewEventProvider;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.response.ResponseInvalidParametersEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidPlayerEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidStateEvent;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.view.VirtualView;

import java.util.List;

public class GameController {

    /**
     * The response event provider
     */
    private final ResponseEventProvider responseEventProvider;

    /**
     * The model of the game
     */
    private Game game;

    /**
     * The virtual view
     */
    private VirtualView virtualView;

    public GameController(IServer server) {
        responseEventProvider = new ResponseEventProvider();

        virtualView = new VirtualView(server, responseEventProvider);

        IViewEventProvider provider = virtualView.getViewEventProvider();
        provider.registerPlayerChallengerSelectGodsEventObserver(this::onChallengerSelectGods);
        provider.registerPlayerChooseGodEventObserver(this::onPlayerChooseGod);
        provider.registerWorkerSpawnEventObserver(this::onWorkerSpawn);
        provider.registerWorkerMoveEventObserver(this::onWorkerMove);
        provider.registerWorkerBuildBlockEventObserver(this::onWorkerBuildBlock);
        provider.registerWorkerBuildDomeEventObserver(this::onWorkerBuildDome);
        provider.registerWorkerForceEventObserver(this::onWorkerForce);
        provider.registerPlayerEndTurnEventObserver(this::onPlayerEndTurn);
    }

    public void selectGame(List<Player> players, Deck deck, boolean simpleGame) {
        this.game = new Game();
        virtualView.selectModelEventProvider(game.getModelEventProvider());
        game.init(players, deck, simpleGame);
    }

    private void onChallengerSelectGods(PlayerChallengerSelectGodsEvent event) {
        if (!checkOrDispatchResponse(event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.selectGods(event.getGods()));
    }

    private void onPlayerChooseGod(PlayerChooseGodEvent event) {
        if (!checkOrDispatchResponse(event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.chooseGod(event.getGod()));
    }

    private void onWorkerSpawn(WorkerSpawnEvent event) {
        if (!checkOrDispatchResponse(event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.spawnWorker(event.getPosition()));
    }

    private void onWorkerMove(WorkerMoveEvent event) {
        if (!checkOrDispatchResponse(event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.moveWorker(event.getId(), event.getDestination()));
    }

    private void onWorkerBuildBlock(WorkerBuildBlockEvent event) {
        if (!checkOrDispatchResponse(event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.buildBlock(event.getId(), event.getDestination()));
    }

    private void onWorkerBuildDome(WorkerBuildDomeEvent event) {
        if (!checkOrDispatchResponse(event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.buildBlock(event.getId(), event.getDestination()));
    }

    private void onWorkerForce(WorkerForceEvent event) {
        if (!checkOrDispatchResponse(event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.forceWorker(event.getId(), event.getTarget(), event.getDestination()));
    }

    private void onPlayerEndTurn(PlayerEndTurnEvent event) {
        if (!checkOrDispatchResponse(event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.endTurn());
    }

    private boolean checkOrDispatchResponse(String player) {
        if (game.getCurrentPlayer().getName().equals(player)) {
            return true;
        }

        responseEventProvider.getResponseInvalidPlayerEventObservable().notifyObservers(
                new ResponseInvalidPlayerEvent(player)
        );
        return false;
    }

    private void dispatchResponseFromModel(String player, Game.ModelResponse response) {
        switch (response) {
            case INVALID_PARAMS -> responseEventProvider.getResponseInvalidParametersEventObservable().notifyObservers(
                    new ResponseInvalidParametersEvent(player)
            );
            case INVALID_STATE -> responseEventProvider.getResponseInvalidStateEventObservable().notifyObservers(
                    new ResponseInvalidStateEvent(player)
            );
        }
    }

}
