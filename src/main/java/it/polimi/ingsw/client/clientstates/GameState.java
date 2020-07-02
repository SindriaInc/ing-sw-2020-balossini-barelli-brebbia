package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.client.data.request.*;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.request.*;
import it.polimi.ingsw.common.event.response.AbstractResponseEvent;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.info.WorkerInfo;
import it.polimi.ingsw.model.Game;

import java.util.*;

public class GameState extends AbstractClientState {

    /**
     * The data used by this state
     */
    private GameData data;

    /**
     * The data before the last view event was sent
     * This is used to restore requests after an invalid message is sent to the server
     */
    private GameData dataSnapshot;

    public GameState(ClientConnector clientConnector, String name, List<String> otherPlayers, boolean simpleGame) {
        super(clientConnector);

        CellInfo[][] map = new CellInfo[Game.BOARD_COLUMNS][Game.BOARD_ROWS];
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                map[x][y] = new CellInfo(0, false);
            }
        }

        this.data = new GameData(null, name, otherPlayers, !simpleGame, false, map, new ArrayList<>());

        getModelEventProvider().registerRequestPlayerChallengerSelectGodsEventObserver(this::onRequestSelectGods);
        getModelEventProvider().registerRequestPlayerChooseGodEventObserver(this::onRequestChooseGod);
        getModelEventProvider().registerRequestPlayerChallengerSelectFirstEventObserver(this::onRequestSelectFirst);
        getModelEventProvider().registerRequestWorkerSpawnEventObserver(this::onRequestSpawn);
        getModelEventProvider().registerRequestWorkerMoveEventObserver(this::onRequestMove);
        getModelEventProvider().registerRequestWorkerBuildBlockEventObserver(this::onRequestBuildBlock);
        getModelEventProvider().registerRequestWorkerBuildDomeEventObserver(this::onRequestBuildDome);
        getModelEventProvider().registerRequestWorkerForceEventObserver(this::onRequestForce);
        getModelEventProvider().registerRequestPlayerEndTurnEventObserver(this::onRequestEndTurn);

        getModelEventProvider().registerPlayerTurnStartEventObserver(this::onPlayerTurnStart);
        getModelEventProvider().registerPlayerChallengerSelectGodsEventObserver(this::onSelectGods);
        getModelEventProvider().registerPlayerChooseGodEventObserver(this::onChooseGod);
        getModelEventProvider().registerPlayerChallengerSelectFirstEventObserver(this::onSelectFirst);
        getModelEventProvider().registerWorkerSpawnEventObserver(this::onSpawn);
        getModelEventProvider().registerWorkerMoveEventObserver(this::onMove);
        getModelEventProvider().registerWorkerBuildBlockEventObserver(this::onBuildBlock);
        getModelEventProvider().registerWorkerBuildDomeEventObserver(this::onBuildDome);
        getModelEventProvider().registerWorkerForceEventObserver(this::onForce);
        getModelEventProvider().registerPlayerLoseEventObserver(this::onLose);
        getModelEventProvider().registerPlayerWinEventObserver(this::onWin);

        getResponseEventProvider().registerResponseInvalidParametersEventObserver(this::onResponse);
        getResponseEventProvider().registerResponseInvalidPlayerEventObserver(this::onResponse);
        getResponseEventProvider().registerResponseInvalidStateEventObserver(this::onResponse);

        updateView();
    }

    public GameData getData() {
        return data;
    }

    /**
     * On a player turn start event update data and view
     * @param event The event
     */
    private void onPlayerTurnStart(PlayerTurnStartEvent event) {
        // The turn start event is sent before requests, the view will be updated after the requests arrive
        data = data.withTurnPlayer(event.getPlayer());

        if (!event.getPlayer().equals(data.getName())) {
            // It's another player's turn, no requests will be sent; The view can be updated
            updateView();
        }
    }

    /*
     * Request events, build the GameData adding the requests
     */

    /**
     * On a request select gods event update data and view
     * @param event The event
     */
    private void onRequestSelectGods(RequestPlayerChallengerSelectGodsEvent event) {
        // The gods phase does not use turn logic
        data = data.withSelectGods(new SelectGodsData(event.getGods(), event.getSelectedGodsCount()));
        dataSnapshot = null;
        updateView();
    }

    /**
     * On a request choose gods event update data and view
     * @param event The event
     */
    private void onRequestChooseGod(RequestPlayerChooseGodEvent event) {
        // The gods phase does not use turn logic
        data = data.withChooseGod(new ChooseGodData(event.getAvailableGods()));
        dataSnapshot = null;
        updateView();
    }

    /**
     * On a request select first event update data and view
     * @param event The event
     */
    private void onRequestSelectFirst(RequestPlayerChallengerSelectFirstEvent event) {
        // The gods phase does not use turn logic
        data = data.withSelectFirst(new SelectFirstData(event.getPlayers()));
        dataSnapshot = null;
        updateView();
    }

    /**
     * On a request spawn event update data and view
     * @param event The event
     */
    private void onRequestSpawn(RequestWorkerSpawnEvent event) {
        // The spawn phase does not use turn logic
        data = data.withSpawn(new InteractData(event.getAvailablePositions()));
        dataSnapshot = null;
        updateView();
    }

    /**
     * On a request move event update data and view
     * @param event The event
     */
    private void onRequestMove(RequestWorkerMoveEvent event) {
        data = data.withMove(extractData(event));
        dataSnapshot = null;
    }

    /**
     * On a request build block event update data and view
     * @param event The event
     */
    private void onRequestBuildBlock(RequestWorkerBuildBlockEvent event) {
        data = data.withBuildBlock(extractData(event));
        dataSnapshot = null;
    }

    /**
     * On a request build dome event update data and view
     * @param event The event
     */
    private void onRequestBuildDome(RequestWorkerBuildDomeEvent event) {
        data = data.withBuildDome(extractData(event));
        dataSnapshot = null;
    }

    /**
     * On a request force event update data and view
     * @param event The event
     */
    private void onRequestForce(RequestWorkerForceEvent event) {
        WorkersOtherInteractData otherInteractData = data.getForceData().orElse(new WorkersOtherInteractData(Map.of()));

        Map<Integer, InteractData> interactions = new HashMap<>();
        for (Map.Entry<Integer, List<Coordinates>> forces : event.getAvailableTargetDestinations().entrySet()) {
            interactions.put(forces.getKey(), new InteractData(forces.getValue()));
        }

        Map<Integer, WorkersInteractData> otherInteractions = new HashMap<>(otherInteractData.getAvailableOtherInteractions());
        otherInteractions.put(event.getWorker(), new WorkersInteractData(interactions));
        otherInteractData = new WorkersOtherInteractData(otherInteractions);

        data = data.withForce(otherInteractData);
        dataSnapshot = null;
    }

    /**
     * On a request end turn event update data and view
     * @param event The event
     */
    private void onRequestEndTurn(RequestPlayerEndTurnEvent event) {
        // Called after each other request event has been called
        data = data.withEndTurn(event.getCanBeEnded());
        dataSnapshot = null;
        updateView();
    }

    /*
     * Model events, update the GameData with the new information
     */

    /**
     * On select gods event update data and view
     * @param event The event
     */
    private void onSelectGods(PlayerChallengerSelectGodsEvent event) {
        // TODO: Show something?

        data = data.withNoRequests();
        dataSnapshot = null;
        updateView();
    }

    /**
     * On choose gods event update data and view
     * @param event The event
     */
    private void onChooseGod(PlayerChooseGodEvent event) {
        // TODO: Show something?

        data = data.withNoRequests();
        dataSnapshot = null;
        updateView();
    }

    /**
     * On select first event update data and view
     * @param event The event
     */
    private void onSelectFirst(PlayerChallengerSelectFirstEvent event) {
        // TODO: Show something?

        // Leave the gods phase
        data = new GameData(null, data.getName(), data.getOtherPlayers(),
                false, data.isSpectating(), data.getMap(), data.getWorkers())
                .withTurnPlayer(data.getTurnPlayer().orElse(null));
        dataSnapshot = null;
        updateView();
    }

    /**
     * On spawn event update data and view
     * @param event The event
     */
    private void onSpawn(WorkerSpawnEvent event) {
        List<WorkerInfo> workers = new ArrayList<>(data.getWorkers());
        workers.add(new WorkerInfo(event.getId(), event.getPlayer(), event.getPosition()));

        data = new GameData(null, data.getName(), data.getOtherPlayers(),
                data.isInGodsPhase(), data.isSpectating(), data.getMap(), workers)
                .withTurnPlayer(data.getTurnPlayer().orElse(null));
        dataSnapshot = null;
        updateView();
    }

    /**
     * On move event update data and view
     * @param event The event
     */
    private void onMove(WorkerMoveEvent event) {
        updatePosition(event.getId(), event.getDestination());
        updateView();
    }

    /**
     * On build block event update data and view
     * @param event The event
     */
    private void onBuildBlock(WorkerBuildBlockEvent event) {
        Optional<CellInfo> cell = data.getCellInfo(event.getDestination());

        if (cell.isEmpty()) {
            throw new IllegalArgumentException("Invalid cell sent by server: " + event.getDestination().toString());
        }

        CellInfo updatedCell = new CellInfo(cell.get().getLevel() + 1, cell.get().isDoomed());
        data = data.withCellInfo(event.getDestination(), updatedCell);
        dataSnapshot = null;
        updateView();
    }

    /**
     * On build dome event update data and view
     * @param event The event
     */
    private void onBuildDome(WorkerBuildDomeEvent event) {
        Optional<CellInfo> cell = data.getCellInfo(event.getDestination());

        if (cell.isEmpty()) {
            throw new IllegalArgumentException("Invalid cell sent by server: " + event.getDestination().toString());
        }

        CellInfo updatedCell = new CellInfo(cell.get().getLevel(), true);
        data = data.withCellInfo(event.getDestination(), updatedCell);
        dataSnapshot = null;
        updateView();
    }

    /**
     * On force event update data and view
     * @param event The event
     */
    private void onForce(WorkerForceEvent event) {
        Optional<CellInfo> cell = data.getCellInfo(event.getDestination());

        if (cell.isEmpty()) {
            throw new IllegalArgumentException("Invalid cell sent by server: " + event.getDestination().toString());
        }

        updatePosition(event.getTarget(), event.getDestination());
    }

    /**
     * On lose event update data and view
     * @param event The event
     */
    private void onLose(PlayerLoseEvent event) {
        if (!event.getPlayer().equals(data.getName())) {
            // TODO: Show players that have lost?
            return;
        }

        data = new GameData(null, data.getName(), data.getOtherPlayers(),
                data.isInGodsPhase(), true, data.getMap(), data.getWorkers());
        updateView();
    }

    /**
     * On win event update data and view
     * @param event The event
     */
    private void onWin(PlayerWinEvent event) {
        getClientConnector().updateState(new EndState(getClientConnector(), data.getName(), event.getPlayer(), data.getMap(), data.getWorkers()));
    }

    /*
     * Response methods
     */

    /**
     * On a response update data and view
     * @param event The event
     */
    private void onResponse(AbstractResponseEvent event) {
        String message = "Invalid action, please specify correct parameters";

        if (dataSnapshot != null) {
            data = dataSnapshot.withMessage(message);
            dataSnapshot = null;
        } else {
            data = data.withMessage(message);
        }

        updateView();
    }

    /*
     * View interaction methods
     */

    /**
     * Update view and send a select gods event
     * @param selectedGods The selected gods
     */
    public void acceptSelectGods(List<String> selectedGods) {
        clearData();
        updateView();
        getClientConnector().send(new PlayerChallengerSelectGodsEvent(getData().getName(), selectedGods));
    }

    /**
     * Update view and send a choose gods event
     * @param chooseGod The chosen god
     */
    public void acceptChooseGod(String chooseGod) {
        clearData();
        updateView();
        getClientConnector().send(new PlayerChooseGodEvent(getData().getName(), chooseGod));
    }

    /**
     * Update view and send a select first event
     * @param first The selected player
     */
    public void acceptSelectFirst(String first) {
        clearData();
        updateView();
        getClientConnector().send(new PlayerChallengerSelectFirstEvent(getData().getName(), first));
    }

    /**
     * Update view and send a worker spawn event
     * @param x The x index
     * @param y The y index
     */
    public void acceptSpawn(int x, int y) {
        clearData();
        updateView();
        getClientConnector().send(new WorkerSpawnEvent(getData().getName(), 0, new Coordinates(x, y)));
    }

    /**
     * Update view and send a move event
     * @param worker The worker
     * @param x The destination's x index
     * @param y The destination's y index
     */
    public void acceptMove(int worker, int x, int y) {
        clearData();
        updateView();
        getClientConnector().send(new WorkerMoveEvent(getData().getName(), worker, new Coordinates(x, y)));
    }

    /**
     * Update view and send a build block event
     * @param worker The worker
     * @param x The target's x index
     * @param y The target's y index
     */
    public void acceptBuildBlock(int worker, int x, int y) {
        clearData();
        updateView();
        getClientConnector().send(new WorkerBuildBlockEvent(getData().getName(), worker, new Coordinates(x, y)));
    }

    /**
     * Update view and send a build dome event
     * @param worker The worker
     * @param x The target's x index
     * @param y The target's y index
     */
    public void acceptBuildDome(int worker, int x, int y) {
        clearData();
        updateView();
        getClientConnector().send(new WorkerBuildDomeEvent(getData().getName(), worker, new Coordinates(x, y)));
    }

    /**
     * Update view and send a build block event
     * @param worker The worker
     * @param target The target worker
     * @param x The target cell x index
     * @param y The target cell y index
     */
    public void acceptForce(int worker, int target, int x, int y) {
        clearData();
        updateView();
        getClientConnector().send(new WorkerForceEvent(getData().getName(), worker, target, new Coordinates(x, y)));
    }

    /**
     * update view and send an end turn event
     */
    public void acceptEnd() {
        clearData();
        updateView();
        getClientConnector().send(new PlayerEndTurnEvent(getData().getName()));
    }

    /*
     * Utility methods
     */

    /**
     * Update the view
     */
    private void updateView() {
        getClientConnector().getViewer().viewGame(this);
    }

    /**
     * Clear the state data
     */
    private void clearData() {
        dataSnapshot = data;
        data = data.withNoRequests();
    }

    /**
     * Update a worker's position
     * @param worker The worker
     * @param position THe new position
     */
    private void updatePosition(int worker, Coordinates position) {
        List<WorkerInfo> workers = new ArrayList<>(data.getWorkers());
        List<WorkerInfo> updatedWorkers = new ArrayList<>();

        for (WorkerInfo workerInfo : workers) {
            WorkerInfo updatedWorker = workerInfo;

            if (worker == workerInfo.getId()) {
                updatedWorker = new WorkerInfo(workerInfo.getId(), workerInfo.getOwner(), position);
            }

            updatedWorkers.add(updatedWorker);
        }

        data = new GameData(null, data.getName(), data.getOtherPlayers(),
                data.isInGodsPhase(), data.isSpectating(), data.getMap(), updatedWorkers)
                .withTurnPlayer(data.getTurnPlayer().orElse(null));
        dataSnapshot = null;
    }

    /**
     * Extract the data from a request worker interact event
     * @param event The event
     * @return The interact data
     */
    private WorkersInteractData extractData(AbstractRequestWorkerInteractEvent event) {
        WorkersInteractData interactData = data.getMoveData().orElse(new WorkersInteractData(Map.of()));

        Map<Integer, InteractData> interactions = new HashMap<>(interactData.getAvailableInteractions());
        interactions.put(event.getWorker(), new InteractData(event.getAvailableDestinations()));
        interactData = new WorkersInteractData(interactions);

        return interactData;
    }

}
