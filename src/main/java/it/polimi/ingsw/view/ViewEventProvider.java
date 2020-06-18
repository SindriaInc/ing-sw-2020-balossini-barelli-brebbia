package it.polimi.ingsw.view;

import it.polimi.ingsw.common.IViewEventProvider;
import it.polimi.ingsw.common.Observable;
import it.polimi.ingsw.common.Observer;
import it.polimi.ingsw.common.event.*;

@SuppressWarnings("unused")
public class ViewEventProvider implements IViewEventProvider {

    /**
     * Observable for PlayerLoginEvent
     */
    private final Observable<PlayerLoginEvent> playerLoginEventObservable = new Observable<>();

    /**
     * Observable for PlayerLoginEvent
     */
    private final Observable<PlayerLogoutEvent> playerLogoutEventObservable = new Observable<>();

    /**
     * Observable for PlayerCreateRoomEvent
     */
    private final Observable<PlayerCreateRoomEvent> playerCreateRoomEventObservable = new Observable<>();

    /**
     * Observable for PlayerJoinRoomEvent
     */
    private final Observable<PlayerJoinRoomEvent> playerJoinRoomEventObservable = new Observable<>();

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
     * Observable for PlayerEndTurnEvent
     */
    private final Observable<PlayerEndTurnEvent> playerEndTurnEventObservable = new Observable<>();

    /**
     * Observable for PlayerPingEvent
     */
    private final Observable<PlayerPingEvent> playerPingEventObservable = new Observable<>();

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
     * @see IViewEventProvider#registerPlayerLoginEventObserver(Observer)
     */
    @Override
    public void registerPlayerLoginEventObserver(Observer<PlayerLoginEvent> observer) {
        playerLoginEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerPlayerLogoutEventObserver(Observer)
     */
    @Override
    public void registerPlayerLogoutEventObserver(Observer<PlayerLogoutEvent> observer) {
        playerLogoutEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerPlayerCreateRoomEventObserver(Observer)
     */
    @Override
    public void registerPlayerCreateRoomEventObserver(Observer<PlayerCreateRoomEvent> observer) {
        playerCreateRoomEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerPlayerJoinRoomEventObserver(Observer)
     */
    @Override
    public void registerPlayerJoinRoomEventObserver(Observer<PlayerJoinRoomEvent> observer) {
        playerJoinRoomEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerPlayerChallengerSelectGodsEventObserver(Observer)
     */
    @Override
    public void registerPlayerChallengerSelectGodsEventObserver(Observer<PlayerChallengerSelectGodsEvent> observer) {
        playerChallengerSelectGodsEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerPlayerChooseGodEventObserver(Observer)
     */
    @Override
    public void registerPlayerChooseGodEventObserver(Observer<PlayerChooseGodEvent> observer) {
        playerChooseGodEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerPlayerChallengerSelectFirstEventObserver(Observer)
     */
    @Override
    public void registerPlayerChallengerSelectFirstEventObserver(Observer<PlayerChallengerSelectFirstEvent> observer) {
        playerChallengerSelectFirstEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerPlayerEndTurnEventObserver(Observer)
     */
    @Override
    public void registerPlayerEndTurnEventObserver(Observer<PlayerEndTurnEvent> observer) {
        playerEndTurnEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerPlayerPingEventObserver(Observer)
     */
    @Override
    public void registerPlayerPingEventObserver(Observer<PlayerPingEvent> observer) {
        playerPingEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerWorkerBuildBlockEventObserver(Observer)
     */
    @Override
    public void registerWorkerBuildBlockEventObserver(Observer<WorkerBuildBlockEvent> observer) {
        workerBuildBlockEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerWorkerBuildDomeEventObserver(Observer)
     */
    @Override
    public void registerWorkerBuildDomeEventObserver(Observer<WorkerBuildDomeEvent> observer) {
        workerBuildDomeEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerWorkerForceEventObserver(Observer)
     */
    @Override
    public void registerWorkerForceEventObserver(Observer<WorkerForceEvent> observer) {
        workerForceEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerWorkerMoveEventObserver(Observer)
     */
    @Override
    public void registerWorkerMoveEventObserver(Observer<WorkerMoveEvent> observer) {
        workerMoveEventObservable.register(observer);
    }

    /**
     * @see IViewEventProvider#registerWorkerSpawnEventObserver(Observer)
     */
    @Override
    public void registerWorkerSpawnEventObserver(Observer<WorkerSpawnEvent> observer) {
        workerSpawnEventObservable.register(observer);
    }

    public Observable<PlayerLoginEvent> getPlayerLoginEventObservable() {
        return playerLoginEventObservable;
    }

    public Observable<PlayerLogoutEvent> getPlayerLogoutEventObservable() {
        return playerLogoutEventObservable;
    }

    public Observable<PlayerCreateRoomEvent> getPlayerCreateRoomEventObservable() {
        return playerCreateRoomEventObservable;
    }

    public Observable<PlayerJoinRoomEvent> getPlayerJoinRoomEventObservable() {
        return playerJoinRoomEventObservable;
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

    public Observable<PlayerEndTurnEvent> getPlayerEndTurnEventObservable() {
        return playerEndTurnEventObservable;
    }

    public Observable<PlayerPingEvent> getPlayerPingEventObservable() {
        return playerPingEventObservable;
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
