package it.polimi.ingsw.model;

import it.polimi.ingsw.common.IModelEventProvider;
import it.polimi.ingsw.common.Observable;
import it.polimi.ingsw.common.Observer;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.lobby.LobbyGameStartEvent;
import it.polimi.ingsw.common.event.lobby.LobbyRoomUpdateEvent;
import it.polimi.ingsw.common.event.lobby.LobbyUpdateEvent;
import it.polimi.ingsw.common.event.request.*;

public class ModelEventProvider implements IModelEventProvider {

    /**
     * Observable for LobbyUpdateEvent
     */
    private final Observable<LobbyUpdateEvent> lobbyUpdateEventObservable = new Observable<>();

    /**
     * Observable for LobbyRoomUpdateEvent
     */
    private final Observable<LobbyRoomUpdateEvent> lobbyRoomUpdateEventObservable = new Observable<>();

    /**
     * Observable for LobbyGameStartEvent
     */
    private final Observable<LobbyGameStartEvent> lobbyGameStartEventObservable = new Observable<>();

    /**
     * Observable for RequestPlayerPingEvent
     */
    private final Observable<RequestPlayerPingEvent> requestPlayerPingEventObservable = new Observable<>();

    /**
     * Observable for RequestPlayerChallengerSelectGodsEvent
     */
    private final Observable<RequestPlayerChallengerSelectGodsEvent> requestPlayerChallengerSelectGodsEventObservable = new Observable<>();

    /**
     * Observable for RequestPlayerChooseGodEvent
     */
    private final Observable<RequestPlayerChooseGodEvent> requestPlayerChooseGodEventObservable = new Observable<>();

    /**
     * Observable for RequestPlayerChallengerSelectFirstEvent
     */
    private final Observable<RequestPlayerChallengerSelectFirstEvent> requestPlayerChallengerSelectFirstEventObservable = new Observable<>();

    /**
     * Observable for RequestPlayerEndTurnEvent
     */
    private final Observable<RequestPlayerEndTurnEvent> requestPlayerEndTurnEventObservable = new Observable<>();

    /**
     * Observable for RequestWorkerBuildBlockEvent
     */
    private final Observable<RequestWorkerBuildBlockEvent> requestWorkerBuildBlockEventObservable = new Observable<>();

    /**
     * Observable for RequestWorkerBuildDomeEvent
     */
    private final Observable<RequestWorkerBuildDomeEvent> requestWorkerBuildDomeEventObservable = new Observable<>();

    /**
     * Observable for RequestWorkerForceEvent
     */
    private final Observable<RequestWorkerForceEvent> requestWorkerForceEventObservable = new Observable<>();

    /**
     * Observable for RequestWorkerMoveEvent
     */
    private final Observable<RequestWorkerMoveEvent> requestWorkerMoveEventObservable = new Observable<>();

    /**
     * Observable for RequestWorkerSpawnEvent
     */
    private final Observable<RequestWorkerSpawnEvent> requestWorkerSpawnEventObservable = new Observable<>();

    /**
     * Observable for PlayerChallengerSelectGodsEvent
     */
    private final Observable<PlayerChallengerSelectGodsEvent> playerChallengerSelectGodsEventObservable = new Observable<>();

    /**
     * Observable for PlayerChooseGodEvent
     */
    private final Observable<PlayerChooseGodEvent> playerChooseGodEventObservable = new Observable<>();

    /**
     * Observable for PlayerChallengerSelectFirstEvent
     */
    private final Observable<PlayerChallengerSelectFirstEvent> playerChallengerSelectFirstEventObservable = new Observable<>();

    /**
     * Observable for PlayerLoseEvent
     */
    private final Observable<PlayerLoseEvent> playerLoseEventObservable = new Observable<>();

    /**
     * Observable for PlayerTurnStartEvent
     */
    private final Observable<PlayerTurnStartEvent> playerTurnStartEventObservable = new Observable<>();

    /**
     * Observable for PlayerWinEvent
     */
    private final Observable<PlayerWinEvent> playerWinEventObservable = new Observable<>();

    /**
     * Observable for WorkerBuildBlockEvent
     */
    private final Observable<WorkerBuildBlockEvent> workerBuildBlockEventObservable = new Observable<>();

    /**
     * Observable for WorkerBuildDomeEvent
     */
    private final Observable<WorkerBuildDomeEvent> workerBuildDomeEventObservable = new Observable<>();

    /**
     * Observable for WorkerForceEvent
     */
    private final Observable<WorkerForceEvent> workerForceEventObservable = new Observable<>();

    /**
     * Observable for WorkerMoveEvent
     */
    private final Observable<WorkerMoveEvent> workerMoveEventObservable = new Observable<>();

    /**
     * Observable for WorkerSpawnEvent
     */
    private final Observable<WorkerSpawnEvent> workerSpawnEventObservable = new Observable<>();

    /**
     * @see IModelEventProvider#registerLobbyUpdateEventObserver(Observer)
     */
    @Override
    public void registerLobbyUpdateEventObserver(Observer<LobbyUpdateEvent> observer) {
        lobbyUpdateEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerLobbyRoomUpdateEventObserver(Observer)
     */
    @Override
    public void registerLobbyRoomUpdateEventObserver(Observer<LobbyRoomUpdateEvent> observer) {
        lobbyRoomUpdateEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerLobbyGameStartEventObserver(Observer)
     */
    @Override
    public void registerLobbyGameStartEventObserver(Observer<LobbyGameStartEvent> observer) {
        lobbyGameStartEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerRequestPlayerPingEventObserver(Observer)
     */
    @Override
    public void registerRequestPlayerPingEventObserver(Observer<RequestPlayerPingEvent> observer) {
        requestPlayerPingEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerRequestPlayerChallengerSelectGodsEventObserver(Observer)
     */
    @Override
    public void registerRequestPlayerChallengerSelectGodsEventObserver(Observer<RequestPlayerChallengerSelectGodsEvent> observer) {
        requestPlayerChallengerSelectGodsEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerRequestPlayerChooseGodEventObserver(Observer)
     */
    @Override
    public void registerRequestPlayerChooseGodEventObserver(Observer<RequestPlayerChooseGodEvent> observer) {
        requestPlayerChooseGodEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerRequestPlayerChallengerSelectFirstEventObserver(Observer)
     */
    @Override
    public void registerRequestPlayerChallengerSelectFirstEventObserver(Observer<RequestPlayerChallengerSelectFirstEvent> observer) {
        requestPlayerChallengerSelectFirstEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerRequestPlayerEndTurnEventObserver(Observer)
     */
    @Override
    public void registerRequestPlayerEndTurnEventObserver(Observer<RequestPlayerEndTurnEvent> observer) {
        requestPlayerEndTurnEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerRequestWorkerBuildBlockEventObserver(Observer)
     */
    @Override
    public void registerRequestWorkerBuildBlockEventObserver(Observer<RequestWorkerBuildBlockEvent> observer) {
        requestWorkerBuildBlockEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerRequestWorkerBuildDomeEventObserver(Observer)
     */
    @Override
    public void registerRequestWorkerBuildDomeEventObserver(Observer<RequestWorkerBuildDomeEvent> observer) {
        requestWorkerBuildDomeEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerRequestWorkerForceEventObserver(Observer)
     */
    @Override
    public void registerRequestWorkerForceEventObserver(Observer<RequestWorkerForceEvent> observer) {
        requestWorkerForceEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerRequestWorkerMoveEventObserver(Observer)
     */
    @Override
    public void registerRequestWorkerMoveEventObserver(Observer<RequestWorkerMoveEvent> observer) {
        requestWorkerMoveEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerRequestWorkerSpawnEventObserver(Observer)
     */
    @Override
    public void registerRequestWorkerSpawnEventObserver(Observer<RequestWorkerSpawnEvent> observer) {
        requestWorkerSpawnEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerPlayerChallengerSelectGodsEventObserver(Observer)
     */
    @Override
    public void registerPlayerChallengerSelectGodsEventObserver(Observer<PlayerChallengerSelectGodsEvent> observer) {
        playerChallengerSelectGodsEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerPlayerChooseGodEventObserver(Observer)
     */
    @Override
    public void registerPlayerChooseGodEventObserver(Observer<PlayerChooseGodEvent> observer) {
        playerChooseGodEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerPlayerChallengerSelectFirstEventObserver(Observer)
     */
    @Override
    public void registerPlayerChallengerSelectFirstEventObserver(Observer<PlayerChallengerSelectFirstEvent> observer) {
        playerChallengerSelectFirstEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerPlayerLoseEventObserver(Observer)
     */
    @Override
    public void registerPlayerLoseEventObserver(Observer<PlayerLoseEvent> observer) {
        playerLoseEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerPlayerTurnStartEventObserver(Observer)
     */
    @Override
    public void registerPlayerTurnStartEventObserver(Observer<PlayerTurnStartEvent> observer) {
        playerTurnStartEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerPlayerWinEventObserver(Observer)
     */
    @Override
    public void registerPlayerWinEventObserver(Observer<PlayerWinEvent> observer) {
        playerWinEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerWorkerBuildBlockEventObserver(Observer)
     */
    @Override
    public void registerWorkerBuildBlockEventObserver(Observer<WorkerBuildBlockEvent> observer) {
        workerBuildBlockEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerWorkerBuildDomeEventObserver(Observer)
     */
    @Override
    public void registerWorkerBuildDomeEventObserver(Observer<WorkerBuildDomeEvent> observer) {
        workerBuildDomeEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerWorkerForceEventObserver(Observer)
     */
    @Override
    public void registerWorkerForceEventObserver(Observer<WorkerForceEvent> observer) {
        workerForceEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerWorkerMoveEventObserver(Observer)
     */
    @Override
    public void registerWorkerMoveEventObserver(Observer<WorkerMoveEvent> observer) {
        workerMoveEventObservable.register(observer);
    }

    /**
     * @see IModelEventProvider#registerWorkerSpawnEventObserver(Observer)
     */
    @Override
    public void registerWorkerSpawnEventObserver(Observer<WorkerSpawnEvent> observer) {
        workerSpawnEventObservable.register(observer);
    }

    public Observable<LobbyUpdateEvent> getLobbyUpdateEventObservable() {
        return lobbyUpdateEventObservable;
    }

    public Observable<LobbyRoomUpdateEvent> getLobbyRoomUpdateEventObservable() {
        return lobbyRoomUpdateEventObservable;
    }

    public Observable<LobbyGameStartEvent> getLobbyGameStartEventObservable() {
        return lobbyGameStartEventObservable;
    }

    public Observable<RequestPlayerPingEvent> getRequestPlayerPingEventObservable() {
        return requestPlayerPingEventObservable;
    }

    public Observable<RequestPlayerChallengerSelectGodsEvent> getRequestPlayerChallengerSelectGodsEventObservable() {
        return requestPlayerChallengerSelectGodsEventObservable;
    }

    public Observable<RequestPlayerChooseGodEvent> getRequestPlayerChooseGodEventObservable() {
        return requestPlayerChooseGodEventObservable;
    }

    public Observable<RequestPlayerChallengerSelectFirstEvent> getRequestPlayerChallengerSelectFirstEventObservable() {
        return requestPlayerChallengerSelectFirstEventObservable;
    }

    public Observable<RequestPlayerEndTurnEvent> getRequestPlayerEndTurnEventObservable() {
        return requestPlayerEndTurnEventObservable;
    }

    public Observable<RequestWorkerBuildBlockEvent> getRequestWorkerBuildBlockEventObservable() {
        return requestWorkerBuildBlockEventObservable;
    }

    public Observable<RequestWorkerBuildDomeEvent> getRequestWorkerBuildDomeEventObservable() {
        return requestWorkerBuildDomeEventObservable;
    }

    public Observable<RequestWorkerForceEvent> getRequestWorkerForceEventObservable() {
        return requestWorkerForceEventObservable;
    }

    public Observable<RequestWorkerMoveEvent> getRequestWorkerMoveEventObservable() {
        return requestWorkerMoveEventObservable;
    }

    public Observable<RequestWorkerSpawnEvent> getRequestWorkerSpawnEventObservable() {
        return requestWorkerSpawnEventObservable;
    }

    public Observable<PlayerChallengerSelectGodsEvent> getPlayerChallengerSelectGodsEventObservable() {
        return playerChallengerSelectGodsEventObservable;
    }

    public Observable<PlayerChooseGodEvent> getPlayerChooseGodEventObservable() {
        return playerChooseGodEventObservable;
    }

    public Observable<PlayerChallengerSelectFirstEvent> getPlayerChallengerSelectFirstEventObservable() {
        return playerChallengerSelectFirstEventObservable;
    }

    public Observable<PlayerLoseEvent> getPlayerLoseEventObservable() {
        return playerLoseEventObservable;
    }

    public Observable<PlayerTurnStartEvent> getPlayerTurnStartEventObservable() {
        return playerTurnStartEventObservable;
    }

    public Observable<PlayerWinEvent> getPlayerWinEventObservable() {
        return playerWinEventObservable;
    }

    public Observable<WorkerBuildBlockEvent> getWorkerBuildBlockEventObservable() {
        return workerBuildBlockEventObservable;
    }

    public Observable<WorkerBuildDomeEvent> getWorkerBuildDomeEventObservable() {
        return workerBuildDomeEventObservable;
    }

    public Observable<WorkerForceEvent> getWorkerForceEventObservable() {
        return workerForceEventObservable;
    }

    public Observable<WorkerMoveEvent> getWorkerMoveEventObservable() {
        return workerMoveEventObservable;
    }

    public Observable<WorkerSpawnEvent> getWorkerSpawnEventObservable() {
        return workerSpawnEventObservable;
    }

}
