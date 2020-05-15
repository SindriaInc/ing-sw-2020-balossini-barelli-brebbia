package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.IViewEventProvider;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.response.ResponseInvalidParametersEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidPlayerEvent;
import it.polimi.ingsw.common.event.response.ResponseInvalidStateEvent;
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

    public Controller(IServer server) {
        responseEventProvider = new ResponseEventProvider();

        lobby = new Lobby();

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
        Game game = getGameOrNull(event.getPlayer());

        if (game == null) {
            return;
        }

        if (!checkOrDispatchResponse(game, event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.selectGods(event.getGods()));
    }

    private void onPlayerChooseGod(PlayerChooseGodEvent event) {
        Game game = getGameOrNull(event.getPlayer());

        if (game == null) {
            return;
        }

        if (!checkOrDispatchResponse(game, event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.chooseGod(event.getGod()));
    }

    private void onWorkerSpawn(WorkerSpawnEvent event) {
        Game game = getGameOrNull(event.getPlayer());

        if (game == null) {
            return;
        }

        if (!checkOrDispatchResponse(game, event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.spawnWorker(event.getPosition()));
    }

    private void onWorkerMove(WorkerMoveEvent event) {
        Game game = getGameOrNull(event.getPlayer());

        if (game == null) {
            return;
        }

        if (!checkOrDispatchResponse(game, event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.moveWorker(event.getId(), event.getDestination()));
    }

    private void onWorkerBuildBlock(WorkerBuildBlockEvent event) {
        Game game = getGameOrNull(event.getPlayer());

        if (game == null) {
            return;
        }

        if (!checkOrDispatchResponse(game, event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.buildBlock(event.getId(), event.getDestination()));
    }

    private void onWorkerBuildDome(WorkerBuildDomeEvent event) {
        Game game = getGameOrNull(event.getPlayer());

        if (game == null) {
            return;
        }

        if (!checkOrDispatchResponse(game, event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.buildDome(event.getId(), event.getDestination()));
    }

    private void onWorkerForce(WorkerForceEvent event) {
        Game game = getGameOrNull(event.getPlayer());

        if (game == null) {
            return;
        }

        if (!checkOrDispatchResponse(game, event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.forceWorker(event.getId(), event.getTarget(), event.getDestination()));
    }

    private void onPlayerEndTurn(PlayerEndTurnEvent event) {
        Game game = getGameOrNull(event.getPlayer());

        if (game == null) {
            return;
        }

        if (!checkOrDispatchResponse(game, event.getPlayer())) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.endTurn());
    }

    private Game getGameOrNull(String player) {
        Optional<Game> optionalGame = lobby.getGame(player);

        if (optionalGame.isEmpty()) {
            dispatchInvalidState(player);
            return null;
        }

        return optionalGame.get();
    }

    private boolean checkOrDispatchResponse(Game game, String player) {
        if (game.getCurrentPlayer().getName().equals(player)) {
            return true;
        }

        responseEventProvider.getResponseInvalidPlayerEventObservable().notifyObservers(
                new ResponseInvalidPlayerEvent(player)
        );
        return false;
    }

    private void dispatchInvalidState(String player) {
        responseEventProvider.getResponseInvalidStateEventObservable().notifyObservers(
                new ResponseInvalidStateEvent(player)
        );
    }

    private void dispatchResponseFromModel(String player, ModelResponse response) {
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
