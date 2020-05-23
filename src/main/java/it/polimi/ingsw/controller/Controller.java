package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.IViewEventProvider;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.response.ResponseInvalidParametersEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidPlayerEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidStateEvent;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.ModelResponse;
import it.polimi.ingsw.server.IServer;
import it.polimi.ingsw.view.VirtualView;

import java.util.Optional;

public class Controller {

    /**
     * The response event provider
     */
    private final ResponseEventProvider responseEventProvider;

    /**
     * The model of the game rooms
     */
    private final Lobby lobby;

    /**
     * The virtual view
     */
    private final VirtualView virtualView;

    public Controller(IServer server, Deck deck) {
        responseEventProvider = new ResponseEventProvider();

        lobby = new Lobby(deck);

        virtualView = new VirtualView(server, lobby.getPlayerChecker(), responseEventProvider);
        virtualView.selectModelEventProvider(lobby.getModelEventProvider());

        IViewEventProvider provider = virtualView.getViewEventProvider();
        provider.registerPlayerLoginEventObserver(this::onPlayerLogin);
        provider.registerPlayerLogoutEventObserver(this::onPlayerLogout);
        provider.registerPlayerCreateRoomEventObserver(this::onPlayerCreateRoom);
        provider.registerPlayerJoinRoomEventObserver(this::onPlayerJoinRoom);
        provider.registerPlayerChallengerSelectGodsEventObserver(this::onChallengerSelectGods);
        provider.registerPlayerChooseGodEventObserver(this::onPlayerChooseGod);
        provider.registerWorkerSpawnEventObserver(this::onWorkerSpawn);
        provider.registerWorkerMoveEventObserver(this::onWorkerMove);
        provider.registerWorkerBuildBlockEventObserver(this::onWorkerBuildBlock);
        provider.registerWorkerBuildDomeEventObserver(this::onWorkerBuildDome);
        provider.registerWorkerForceEventObserver(this::onWorkerForce);
        provider.registerPlayerEndTurnEventObserver(this::onPlayerEndTurn);
    }

    /**
     * Handle server shutdown
     */
    public void shutdown() {
        virtualView.shutdown();
    }

    private void onPlayerLogin(PlayerLoginEvent event) {
        dispatchResponseFromModel(event.getPlayer(), lobby.login(event.getPlayer(), event.getAge()));
    }

    private void onPlayerLogout(PlayerLogoutEvent event) {
        dispatchResponseFromModel(event.getPlayer(), lobby.logout(event.getPlayer()));
    }

    private void onPlayerCreateRoom(PlayerCreateRoomEvent event) {
        dispatchResponseFromModel(event.getPlayer(), lobby.createRoom(event.getPlayer(), event.getMaxPlayers(), event.isSimpleGame()));
    }

    private void onPlayerJoinRoom(PlayerJoinRoomEvent event) {
        dispatchResponseFromModel(event.getPlayer(), lobby.joinRoom(event.getPlayer(), event.getRoomOwner()));
    }

    private void onChallengerSelectGods(PlayerChallengerSelectGodsEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().selectGods(event.getGods()));
    }

    private void onPlayerChooseGod(PlayerChooseGodEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().chooseGod(event.getGod()));
    }

    private void onWorkerSpawn(WorkerSpawnEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().spawnWorker(event.getPosition()));
    }

    private void onWorkerMove(WorkerMoveEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().moveWorker(event.getId(), event.getDestination()));
    }

    private void onWorkerBuildBlock(WorkerBuildBlockEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().buildBlock(event.getId(), event.getDestination()));
    }

    private void onWorkerBuildDome(WorkerBuildDomeEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().buildDome(event.getId(), event.getDestination()));
    }

    private void onWorkerForce(WorkerForceEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().forceWorker(event.getId(), event.getTarget(), event.getDestination()));
    }

    private void onPlayerEndTurn(PlayerEndTurnEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().endTurn());
    }

    private Optional<Game> getGameOrDispatchResponse(String player) {
        Optional<Game> optionalGame = lobby.getGame(player);

        if (optionalGame.isEmpty()) {
            new ResponseInvalidStateEvent(player).accept(responseEventProvider);
            return Optional.empty();
        }

        if (!optionalGame.get().getCurrentPlayer().getName().equals(player)) {
            new ResponseInvalidPlayerEvent(player).accept(responseEventProvider);
            return Optional.empty();
        }

        return optionalGame;
    }

    private boolean checkOrDispatchResponse(Game game, String player) {
        if (game.getCurrentPlayer().getName().equals(player)) {
            return true;
        }

        new ResponseInvalidPlayerEvent(player).accept(responseEventProvider);
        return false;
    }

    private void dispatchResponseFromModel(String player, ModelResponse response) {
        switch (response) {
            case INVALID_PARAMS -> new ResponseInvalidParametersEvent(player).accept(responseEventProvider);
            case INVALID_STATE -> new ResponseInvalidStateEvent(player).accept(responseEventProvider);
        }
    }

}
