package it.polimi.ingsw.view;

import it.polimi.ingsw.common.ModelEventProvider;
import it.polimi.ingsw.common.events.*;
import it.polimi.ingsw.common.events.requests.*;

public abstract class VirtualView {

    public VirtualView(ModelEventProvider provider) {
        provider.registerRequestChallengerSelectGodsEventObserver(this::onRequestChallengerSelectGods);
        provider.registerRequestPlayerChooseGodEventObserver(this::onRequestPlayerChooseGod);
        provider.registerRequestPlayerEndTurnEventObserver(this::onRequestPlayerEndTurn);
        provider.registerRequestWorkerBuildBlockEventObserver(this::onRequestWorkerBuildBlock);
        provider.registerRequestWorkerBuildDomeEventObserver(this::onRequestWorkerBuildDome);
        provider.registerRequestWorkerForceEventObserver(this::onRequestWorkerForce);
        provider.registerRequestWorkerMoveEventObserver(this::onRequestWorkerMove);
        provider.registerRequestWorkerSpawnEventObserver(this::onRequestWorkerSpawn);

        provider.registerChallengerSelectGodsEventObserver(this::onChallengerSelectGods);
        provider.registerPlayerChooseGodEventObserver(this::onPlayerChooseGod);
        provider.registerPlayerLoseEventObserver(this::onPlayerLose);
        provider.registerPlayerTurnStartEventObserver(this::onPlayerTurnStart);
        provider.registerPlayerWinEventObserver(this::onPlayerWin);
        provider.registerWorkerBuildBlockEventObserver(this::onWorkerBuildBlock);
        provider.registerWorkerBuildDomeEventObserver(this::onWorkerBuildDome);
        provider.registerWorkerForceEventObserver(this::onWorkerForce);
        provider.registerWorkerSpawnEventObserver(this::onWorkerSpawn);
    }

    private void onRequestChallengerSelectGods(RequestChallengerSelectGodsEvent requestChallengerSelectGodsEvent) { }

    private void onRequestPlayerChooseGod(RequestPlayerChooseGodEvent requestPlayerChooseGodEvent) { }

    private void onRequestPlayerEndTurn(RequestPlayerEndTurnEvent requestPlayerEndTurnEvent) { }

    private void onRequestWorkerBuildBlock(RequestWorkerBuildBlockEvent requestWorkerBuildBlockEvent) { }

    private void onRequestWorkerBuildDome(RequestWorkerBuildDomeEvent requestWorkerBuildDomeEvent) { }

    private void onRequestWorkerForce(RequestWorkerForceEvent requestWorkerForceEvent) { }

    private void onRequestWorkerMove(RequestWorkerMoveEvent requestWorkerMoveEvent) { }

    private void onRequestWorkerSpawn(RequestWorkerSpawnEvent requestWorkerSpawnEvent) { }

    private void onPlayerTurnStart(PlayerTurnStartEvent event) { }

    private void onChallengerSelectGods(ChallengerSelectGodsEvent event) { }

    private void onPlayerChooseGod(PlayerChooseGodEvent event) { }

    private void onPlayerLose(PlayerLoseEvent event) { }

    private void onPlayerWin(PlayerWinEvent event) { }

    private void onWorkerBuildBlock(WorkerBuildBlockEvent event) { }

    private void onWorkerBuildDome(WorkerBuildDomeEvent event) { }

    private void onWorkerForce(WorkerForceEvent event) { }

    private void onWorkerSpawn(WorkerSpawnEvent event) { }

}
