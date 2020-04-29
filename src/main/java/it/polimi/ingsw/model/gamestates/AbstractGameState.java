package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.ModelEventProvider;
import it.polimi.ingsw.common.Observable;
import it.polimi.ingsw.common.Observer;
import it.polimi.ingsw.common.events.*;
import it.polimi.ingsw.common.events.requests.*;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractGameState implements ModelEventProvider {

    /**
     * The Board of this game
     */
    private final Board board;

    /**
     * List of the players in the game
     * The order of the players is preserved
     *
     * When a player loses it gets removed from this list
     */
    private final List<Player> activePlayers = new LinkedList<>();

    /**
     * Observer for RequestChallengerSelectGodsEvent
     */
    private final Observable<RequestChallengerSelectGodsEvent> requestChallengerSelectGodsEventObservable = new Observable<>();

    /**
     * Observer for RequestPlayerChooseGodEvent
     */
    private final Observable<RequestPlayerChooseGodEvent> requestPlayerChooseGodEventObservable = new Observable<>();

    /**
     * Observer for RequestPlayerEndTurnEvent
     */
    private final Observable<RequestPlayerEndTurnEvent> requestPlayerEndTurnEventObservable = new Observable<>();

    /**
     * Observer for RequestWorkerBuildBlockEvent
     */
    private final Observable<RequestWorkerBuildBlockEvent> requestWorkerBuildBlockEventObservable = new Observable<>();

    /**
     * Observer for RequestWorkerBuildDomeEvent
     */
    private final Observable<RequestWorkerBuildDomeEvent> requestWorkerBuildDomeEventObservable = new Observable<>();

    /**
     * Observer for RequestWorkerForceEvent
     */
    private final Observable<RequestWorkerForceEvent> requestWorkerForceEventObservable = new Observable<>();

    /**
     * Observer for RequestWorkerMoveEvent
     */
    private final Observable<RequestWorkerMoveEvent> requestWorkerMoveEventObservable = new Observable<>();

    /**
     * Observer for RequestWorkerSpawnEvent
     */
    private final Observable<RequestWorkerSpawnEvent> requestWorkerSpawnEventObservable = new Observable<>();

    /**
     * Observer for ChallengerSelectGodsEvent
     */
    private final Observable<ChallengerSelectGodsEvent> challengerSelectGodsEventObservable = new Observable<>();

    /**
     * Observer for PlayerChooseGodEvent
     */
    private final Observable<PlayerChooseGodEvent> playerChooseGodEventObservable = new Observable<>();

    /**
     * Observer for PlayerEndTurnEvent
     */
    private final Observable<PlayerEndTurnEvent> playerEndTurnEventObservable = new Observable<>();

    /**
     * Observer for PlayerLoseEvent
     */
    private final Observable<PlayerLoseEvent> playerLoseEventObservable = new Observable<>();

    /**
     * Observer for PlayerTurnStartEvent
     */
    private final Observable<PlayerTurnStartEvent> playerTurnStartEventObservable = new Observable<>();

    /**
     * Observer for PlayerWinEvent
     */
    private final Observable<PlayerWinEvent> playerWinEventObservable = new Observable<>();

    /**
     * Observer for WorkerBuildBlockEvent
     */
    private final Observable<WorkerBuildBlockEvent> workerBuildBlockEventObservable = new Observable<>();

    /**
     * Observer for WorkerBuildDomeEvent
     */
    private final Observable<WorkerBuildDomeEvent> workerBuildDomeEventObservable = new Observable<>();

    /**
     * Observer for WorkerForceEvent
     */
    private final Observable<WorkerForceEvent> workerForceEventObservable = new Observable<>();

    /**
     * Observer for WorkerMoveEvent
     */
    private final Observable<WorkerMoveEvent> workerMoveEventObservable = new Observable<>();

    /**
     * Observer for WorkerSpawnEvent
     */
    private final Observable<WorkerSpawnEvent> workerSpawnEventObservable = new Observable<>();

    public AbstractGameState(Board board, List<Player> players) {
        this.board = board;
        this.activePlayers.addAll(players);
    }

    public Game.ModelResponse selectGods(List<String> gods) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse chooseGod(String god) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse spawnWorker(Coordinates position) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse moveWorker(int worker, Coordinates destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse buildBlock(int worker, Coordinates destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse buildDome(int worker, Coordinates destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse forceWorker(int worker, int target, Coordinates destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse endTurn() {
        return Game.ModelResponse.INVALID_STATE;
    }

    public final boolean checkHasLost(Player player) {
        return !activePlayers.contains(player);
    }

    public final Board getBoard() {
        return board;
    }

    public final List<Player> getPlayers() {
        return List.copyOf(activePlayers);
    }

    public final List<Player> getOpponents(Player player) {
        List<Player> opponents = new ArrayList<>(activePlayers);
        opponents.remove(player);

        return opponents;
    }

    @Override
    public void registerRequestChallengerSelectGodsEventObserver(Observer<RequestChallengerSelectGodsEvent> observer) {
        requestChallengerSelectGodsEventObservable.register(observer);
    }

    @Override
    public void registerRequestPlayerChooseGodEventObserver(Observer<RequestPlayerChooseGodEvent> observer) {
        requestPlayerChooseGodEventObservable.register(observer);
    }

    @Override
    public void registerRequestPlayerEndTurnEventObserver(Observer<RequestPlayerEndTurnEvent> observer) {
        requestPlayerEndTurnEventObservable.register(observer);
    }

    @Override
    public void registerRequestWorkerBuildBlockEventObserver(Observer<RequestWorkerBuildBlockEvent> observer) {
        requestWorkerBuildBlockEventObservable.register(observer);
    }

    @Override
    public void registerRequestWorkerBuildDomeEventObserver(Observer<RequestWorkerBuildDomeEvent> observer) {
        requestWorkerBuildDomeEventObservable.register(observer);
    }

    @Override
    public void registerRequestWorkerForceEventObserver(Observer<RequestWorkerForceEvent> observer) {
        requestWorkerForceEventObservable.register(observer);
    }

    @Override
    public void registerRequestWorkerMoveEventObserver(Observer<RequestWorkerMoveEvent> observer) {
        requestWorkerMoveEventObservable.register(observer);
    }

    @Override
    public void registerRequestWorkerSpawnEventObserver(Observer<RequestWorkerSpawnEvent> observer) {
        requestWorkerSpawnEventObservable.register(observer);
    }

    @Override
    public void registerChallengerSelectGodsEventObserver(Observer<ChallengerSelectGodsEvent> observer) {
        challengerSelectGodsEventObservable.register(observer);
    }

    @Override
    public void registerPlayerChooseGodEventObserver(Observer<PlayerChooseGodEvent> observer) {
        playerChooseGodEventObservable.register(observer);
    }

    @Override
    public void registerPlayerEndTurnEventObserver(Observer<PlayerEndTurnEvent> observer) {
        playerEndTurnEventObservable.register(observer);
    }

    @Override
    public void registerPlayerLoseEventObserver(Observer<PlayerLoseEvent> observer) {
        playerLoseEventObservable.register(observer);
    }

    @Override
    public void registerPlayerTurnStartEventObserver(Observer<PlayerTurnStartEvent> observer) {
        playerTurnStartEventObservable.register(observer);
    }

    @Override
    public void registerPlayerWinEventObserver(Observer<PlayerWinEvent> observer) {
        playerWinEventObservable.register(observer);
    }

    @Override
    public void registerWorkerBuildBlockEventObserver(Observer<WorkerBuildBlockEvent> observer) {
        workerBuildBlockEventObservable.register(observer);
    }

    @Override
    public void registerWorkerBuildDomeEventObserver(Observer<WorkerBuildDomeEvent> observer) {
        workerBuildDomeEventObservable.register(observer);
    }

    @Override
    public void registerWorkerForceEventObserver(Observer<WorkerForceEvent> observer) {
        workerForceEventObservable.register(observer);
    }

    @Override
    public void registerWorkerMoveEventObserver(Observer<WorkerMoveEvent> observer) {
        workerMoveEventObservable.register(observer);
    }

    @Override
    public void registerWorkerSpawnEventObserver(Observer<WorkerSpawnEvent> observer) {
        workerSpawnEventObservable.register(observer);
    }

    final Observable<RequestChallengerSelectGodsEvent> getRequestChallengerSelectGodsEventObservable() {
        return requestChallengerSelectGodsEventObservable;
    }

    final Observable<RequestPlayerChooseGodEvent> getRequestPlayerChooseGodEventObservable() {
        return requestPlayerChooseGodEventObservable;
    }

    final Observable<RequestPlayerEndTurnEvent> getRequestPlayerEndTurnEventObservable() {
        return requestPlayerEndTurnEventObservable;
    }

    final Observable<RequestWorkerBuildBlockEvent> getRequestWorkerBuildBlockEventObservable() {
        return requestWorkerBuildBlockEventObservable;
    }

    final Observable<RequestWorkerBuildDomeEvent> getRequestWorkerBuildDomeEventObservable() {
        return requestWorkerBuildDomeEventObservable;
    }

    final Observable<RequestWorkerForceEvent> getRequestWorkerForceEventObservable() {
        return requestWorkerForceEventObservable;
    }

    final Observable<RequestWorkerMoveEvent> getRequestWorkerMoveEventObservable() {
        return requestWorkerMoveEventObservable;
    }

    final Observable<RequestWorkerSpawnEvent> getRequestWorkerSpawnEventObservable() {
        return requestWorkerSpawnEventObservable;
    }

    final Observable<ChallengerSelectGodsEvent> getChallengerSelectGodsEventObservable() {
        return challengerSelectGodsEventObservable;
    }

    final Observable<PlayerChooseGodEvent> getPlayerChooseGodEventObservable() {
        return playerChooseGodEventObservable;
    }

    final Observable<PlayerEndTurnEvent> getPlayerEndTurnEventObservable() {
        return playerEndTurnEventObservable;
    }

    final Observable<PlayerLoseEvent> getPlayerLoseEventObservable() {
        return playerLoseEventObservable;
    }

    final Observable<PlayerTurnStartEvent> getPlayerTurnStartEventObservable() {
        return playerTurnStartEventObservable;
    }

    final Observable<PlayerWinEvent> getPlayerWinEventObservable() {
        return playerWinEventObservable;
    }

    final Observable<WorkerBuildBlockEvent> getWorkerBuildBlockEventObservable() {
        return workerBuildBlockEventObservable;
    }

    final Observable<WorkerBuildDomeEvent> getWorkerBuildDomeEventObservable() {
        return workerBuildDomeEventObservable;
    }

    final Observable<WorkerForceEvent> getWorkerForceEventObservable() {
        return workerForceEventObservable;
    }

    final Observable<WorkerMoveEvent> getWorkerMoveEventObservable() {
        return workerMoveEventObservable;
    }

    final Observable<WorkerSpawnEvent> getWorkerSpawnEventObservable() {
        return workerSpawnEventObservable;
    }

    /**
     * Remove the player from the players list
     */
    final void removePlayer(Player player) {
        this.activePlayers.remove(player);
    }

    /**
     * Update the list of players with the given sorted list
     * The list must contain each and every player originally present
     */
    final void sortPlayers(List<Player> players) {
        if (players.size() != this.activePlayers.size() || !players.containsAll(this.activePlayers) || !this.activePlayers.containsAll(players)) {
            throw new IllegalArgumentException("The new player list is not a sort of the original list");
        }

        this.activePlayers.clear();
        this.activePlayers.addAll(players);
    }

    /**
     * Obtain the current player that is able to interact with the game
     * Calling this method repeatedly should not result in a different player unless other methods got called
     * If the game has ended, this method must return the winner
     * @return The Player
     */
    public abstract Player getCurrentPlayer();

    /**
     * Obtain the next state of the game
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractGameState
     */
    public abstract AbstractGameState nextState();

}
