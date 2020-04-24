package it.polimi.ingsw.model.gamestates;

import it.polimi.ingsw.common.Observable;
import it.polimi.ingsw.common.Observer;
import it.polimi.ingsw.common.events.*;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractGameState {

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
     * Observer for ChallengerSelectGodsEvent
     */
    private final Observable<ChallengerSelectGodsEvent> challengerSelectGodsEventObservable = new Observable<>();

    /**
     * Observer for PlayerChooseGodEvent
     */
    private final Observable<PlayerChooseGodEvent> playerChooseGodEventObservable = new Observable<>();

    /**
     * Observer for PlayerLoseEvent
     */
    private final Observable<PlayerLoseEvent> playerLoseEventObservable = new Observable<>();

    /**
     * Observer for PlayerTurnStartEvent
     */
    private final Observable<PlayerTurnStartEvent> playerTurnStartEventObservable = new Observable<>();

    /**
     * Observer for PlayerWinListener
     */
    private final Observable<PlayerWinListener> playerWinListenerObservable = new Observable<>();

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

    public List<God> getAvailableGods() {
        return null;
    }

    public Integer getSelectGodsCount() {
        return null;
    }

    public boolean checkCanSelectGods(List<God> gods) {
        return false;
    }

    public Game.ModelResponse selectGods(List<God> gods) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public Game.ModelResponse chooseGod(God god) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public List<Cell> getAvailableCells() {
        return null;
    }

    public Game.ModelResponse spawnWorker(Worker worker) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public List<Cell> getAvailableMoves(Worker worker) {
        return null;
    }

    public Game.ModelResponse moveWorker(Worker worker, Cell destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public List<Cell> getAvailableBlockBuilds(Worker worker) {
        return null;
    }

    public Game.ModelResponse buildBlock(Worker worker, Cell destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public List<Cell> getAvailableDomeBuilds(Worker worker) {
        return null;
    }

    public Game.ModelResponse buildDome(Worker worker, Cell destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public List<Cell> getAvailableForces(Worker worker, Worker target) {
        return null;
    }

    public Game.ModelResponse forceWorker(Worker worker, Worker target, Cell destination) {
        return Game.ModelResponse.INVALID_STATE;
    }

    public boolean checkCanEndTurn() {
        return false;
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

    public void registerChallengerSelectGodsEventObserver(Observer<ChallengerSelectGodsEvent> observer) {
        challengerSelectGodsEventObservable.register(observer);
    }

    public void registerPlayerChooseGodEventObserver(Observer<PlayerChooseGodEvent> observer) {
        playerChooseGodEventObservable.register(observer);
    }

    public void registerPlayerLoseEventObserver(Observer<PlayerLoseEvent> observer) {
        playerLoseEventObservable.register(observer);
    }

    public void registerPlayerTurnStartEventObserver(Observer<PlayerTurnStartEvent> observer) {
        playerTurnStartEventObservable.register(observer);
    }

    public void registerPlayerWinListenerObserver(Observer<PlayerWinListener> observer) {
        playerWinListenerObservable.register(observer);
    }

    public void registerWorkerBuildBlockEventObserver(Observer<WorkerBuildBlockEvent> observer) {
        workerBuildBlockEventObservable.register(observer);
    }

    public void registerWorkerBuildDomeEventObserver(Observer<WorkerBuildDomeEvent> observer) {
        workerBuildDomeEventObservable.register(observer);
    }

    public void registerWorkerForceEventObserver(Observer<WorkerForceEvent> observer) {
        workerForceEventObservable.register(observer);
    }

    public void registerWorkerMoveEventObserver(Observer<WorkerMoveEvent> observer) {
        workerMoveEventObservable.register(observer);
    }

    public void registerWorkerSpawnEventObserver(Observer<WorkerSpawnEvent> observer) {
        workerSpawnEventObservable.register(observer);
    }

    final Observable<ChallengerSelectGodsEvent> getChallengerSelectGodsEventObservable() {
        return challengerSelectGodsEventObservable;
    }

    final Observable<PlayerChooseGodEvent> getPlayerChooseGodEventObservable() {
        return playerChooseGodEventObservable;
    }

    final Observable<PlayerLoseEvent> getPlayerLoseEventObservable() {
        return playerLoseEventObservable;
    }

    final Observable<PlayerTurnStartEvent> getPlayerTurnStartEventObservable() {
        return playerTurnStartEventObservable;
    }

    final Observable<PlayerWinListener> getPlayerWinListenerObservable() {
        return playerWinListenerObservable;
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
     * If AbstractGameState#isEnded returns true, this method must return the winner
     * @return The Player
     */
    public abstract Player getCurrentPlayer();

    /**
     * Obtain the next state of the game
     * Calling this method repeatedly should not result in a different state unless other methods got called
     * @return The AbstractGameState
     */
    public abstract AbstractGameState nextState();

    /**
     * Check if the game has ended, meaning that one of the player has won
     * If this method returns true, AbstractGameState#getCurrentPlayer must return the winner
     * @return true if there is a winner
     *
     * <strong>This method must have no side effect</strong>
     */
    public abstract boolean isEnded();

}
