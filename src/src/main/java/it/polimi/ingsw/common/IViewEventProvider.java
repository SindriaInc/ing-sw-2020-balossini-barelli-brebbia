package it.polimi.ingsw.common;

import it.polimi.ingsw.common.event.*;

/**
 * The provider that handles the registration of view events
 *
 * View events are events sent by a view (in the client)
 */
public interface IViewEventProvider {

    /**
     * Register the observer for PlayerLoginEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerLoginEventObserver(Observer<PlayerLoginEvent> observer);

    /**
     * Register the observer for PlayerLogoutEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerLogoutEventObserver(Observer<PlayerLogoutEvent> observer);

    /**
     * Register the observer for PlayerCreateRoomEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerCreateRoomEventObserver(Observer<PlayerCreateRoomEvent> observer);

    /**
     * Register the observer for PlayerJoinRoomEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerJoinRoomEventObserver(Observer<PlayerJoinRoomEvent> observer);

    /**
     * Register the observer for PlayerChallengerSelectGodsEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerChallengerSelectGodsEventObserver(Observer<PlayerChallengerSelectGodsEvent> observer);

    /**
     * Register the observer for PlayerChooseGodEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerChooseGodEventObserver(Observer<PlayerChooseGodEvent> observer);

    /**
     * Register the observer for PlayerChallengerSelectFirstEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerChallengerSelectFirstEventObserver(Observer<PlayerChallengerSelectFirstEvent> observer);

    /**
     * Register the observer for PlayerEndTurnEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerEndTurnEventObserver(Observer<PlayerEndTurnEvent> observer);

    /**
     * Register the observer for PlayerPingEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerPingEventObserver(Observer<PlayerPingEvent> observer);

    /**
     * Register the observer for WorkerBuildBlockEvent in the related observable
     * @param observer The Observer
     */
    void registerWorkerBuildBlockEventObserver(Observer<WorkerBuildBlockEvent> observer);

    /**
     * Register the observer for WorkerBuildDomeEvent in the related observable
     * @param observer The Observer
     */
    void registerWorkerBuildDomeEventObserver(Observer<WorkerBuildDomeEvent> observer);

    /**
     * Register the observer for WorkerForceEvent in the related observable
     * @param observer The Observer
     */
    void registerWorkerForceEventObserver(Observer<WorkerForceEvent> observer);

    /**
     * Register the observer for WorkerMoveEvent in the related observable
     * @param observer The Observer
     */
    void registerWorkerMoveEventObserver(Observer<WorkerMoveEvent> observer);

    /**
     * Register the observer for WorkerSpawnEvent in the related observable
     * @param observer The Observer
     */
    void registerWorkerSpawnEventObserver(Observer<WorkerSpawnEvent> observer);

}
