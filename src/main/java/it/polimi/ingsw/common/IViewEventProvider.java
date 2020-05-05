package it.polimi.ingsw.common;

import it.polimi.ingsw.common.event.*;

public interface IViewEventProvider {

    /**
     * Register the observer for ChallengerSelectGodsEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerChallengerSelectGodsEventObserver(Observer<PlayerChallengerSelectGodsEvent> observer);

    /**
     * Register the observer for PlayerChooseGodEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerChooseGodEventObserver(Observer<PlayerChooseGodEvent> observer);

    /**
     * Register the observer for PlayerEndTurnEvent in the related observable
     * @param observer The Observer
     */
    void registerPlayerEndTurnEventObserver(Observer<PlayerEndTurnEvent> observer);

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
