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

/**
 * The Controller handles events coming from the <code>VirtualView</code>
 *
 * Events are validated to make sure that they are coming from a player that can send those events, and are then
 * used to call the appropriate model methods
 *
 * Invalid events are rejected, sending an appropriate <code>AbstractResponseEvent</code> to the client
 */
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

    /**
     * Class constructor, initializes the <code>VirtualView</code> and registers observers for view events
     *
     * @param server The server instance, used by the <code>VirtualView</code>
     * @param deck The deck to be used for the games
     */
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
        provider.registerPlayerChallengerSelectFirstEventObserver(this::onChallengerSelectFirst);
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

    /**
     * Handles a <code>PlayerLoginEvent</code>
     *
     * @param event The event
     */
    private void onPlayerLogin(PlayerLoginEvent event) {
        dispatchResponseFromModel(event.getPlayer(), lobby.login(event.getPlayer(), event.getAge()));
    }

    /**
     * Handles a <code>PlayerLogoutEvent</code>
     *
     * @param event The event
     */
    private void onPlayerLogout(PlayerLogoutEvent event) {
        dispatchResponseFromModel(event.getPlayer(), lobby.logout(event.getPlayer()));
    }

    /**
     * Handles a <code>PlayerCreateRoomEvent</code>
     *
     * @param event The event
     */
    private void onPlayerCreateRoom(PlayerCreateRoomEvent event) {
        dispatchResponseFromModel(event.getPlayer(), lobby.createRoom(event.getPlayer(), event.getMaxPlayers(), event.isSimpleGame()));
    }

    /**
     * Handles a <code>PlayerJoinRoomEvent</code>
     *
     * @param event The event
     */
    private void onPlayerJoinRoom(PlayerJoinRoomEvent event) {
        dispatchResponseFromModel(event.getPlayer(), lobby.joinRoom(event.getPlayer(), event.getRoomOwner()));
    }

    /**
     * Handles a <code>PlayerChallengerSelectGodsEvent</code>
     * The event is rejected if the player is not in a game
     *
     * @param event The event
     */
    private void onChallengerSelectGods(PlayerChallengerSelectGodsEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().selectGods(event.getGods()));
    }

    /**
     * Handles a <code>PlayerChooseGodEvent</code>
     * The event is rejected if the player is not in a game
     *
     * @param event The event
     */
    private void onPlayerChooseGod(PlayerChooseGodEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().chooseGod(event.getGod()));
    }

    /**
     * Handles a <code>PlayerChallengerSelectFirstEvent</code>
     * The event is rejected if the player is not in a game
     *
     * @param event The event
     */
    private void onChallengerSelectFirst(PlayerChallengerSelectFirstEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().selectFirst(event.getFirst()));
    }

    /**
     * Handles a <code>WorkerSpawnEvent</code>
     * The event is rejected if the player is not in a game
     *
     * @param event The event
     */
    private void onWorkerSpawn(WorkerSpawnEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().spawnWorker(event.getPosition()));
    }

    /**
     * Handles a <code>WorkerMoveEvent</code>
     * The event is rejected if the player is not in a game
     *
     * @param event The event
     */
    private void onWorkerMove(WorkerMoveEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().moveWorker(event.getId(), event.getDestination()));
    }

    /**
     * Handles a <code>WorkerBuildBlockEvent</code>
     * The event is rejected if the player is not in a game
     *
     * @param event The event
     */
    private void onWorkerBuildBlock(WorkerBuildBlockEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().buildBlock(event.getId(), event.getDestination()));
    }

    /**
     * Handles a <code>WorkerBuildDomeEvent</code>
     * The event is rejected if the player is not in a game
     *
     * @param event The event
     */
    private void onWorkerBuildDome(WorkerBuildDomeEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().buildDome(event.getId(), event.getDestination()));
    }

    /**
     * Handles a <code>WorkerForceEvent</code>
     * The event is rejected if the player is not in a game
     *
     * @param event The event
     */
    private void onWorkerForce(WorkerForceEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().forceWorker(event.getId(), event.getTarget(), event.getDestination()));
    }

    /**
     * Handles a <code>PlayerEndTurnEvent</code>
     * The event is rejected if the player is not in a game
     *
     * @param event The event
     */
    private void onPlayerEndTurn(PlayerEndTurnEvent event) {
        Optional<Game> game = getGameOrDispatchResponse(event.getPlayer());

        if (game.isEmpty()) {
            return;
        }

        dispatchResponseFromModel(event.getPlayer(), game.get().endTurn());
    }

    /**
     * Obtains the game instance that the player is playing on and it's their turn to play on
     * Dispaches a <code>ResponseInvalidStateEvent</code> on every <code>Optional.empty()</code> return condition
     *
     * @param player The player
     * @return The game instance, or <code>Optional.empty()</code> if the player is not in a game or it's not their turn
     */
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

    /**
     * Dispatches an appropriate <code>AbstractResponseEvent</code> based on the <code>ModelResponse</code>
     *
     * @param player The player
     * @param response The model response
     */
    private void dispatchResponseFromModel(String player, ModelResponse response) {
        switch (response) {
            case INVALID_PARAMS -> new ResponseInvalidParametersEvent(player).accept(responseEventProvider);
            case INVALID_STATE -> new ResponseInvalidStateEvent(player).accept(responseEventProvider);
        }
    }

}
