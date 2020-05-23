package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.client.data.GameData;
import it.polimi.ingsw.client.data.request.*;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.request.*;
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
        getModelEventProvider().registerRequestWorkerSpawnEventObserver(this::onRequestSpawn);
        getModelEventProvider().registerRequestWorkerMoveEventObserver(this::onRequestMove);
        getModelEventProvider().registerRequestWorkerBuildBlockEventObserver(this::onRequestBuildBlock);
        getModelEventProvider().registerRequestWorkerBuildDomeEventObserver(this::onRequestBuildDome);
        getModelEventProvider().registerRequestWorkerForceEventObserver(this::onRequestForce);
        getModelEventProvider().registerRequestPlayerEndTurnEventObserver(this::onRequestEndTurn);

        getModelEventProvider().registerPlayerTurnStartEventObserver(this::onPlayerTurnStart);
        getModelEventProvider().registerPlayerChallengerSelectGodsEventObserver(this::onSelectGods);
        getModelEventProvider().registerPlayerChooseGodEventObserver(this::onChooseGod);
        getModelEventProvider().registerWorkerSpawnEventObserver(this::onSpawn);
        getModelEventProvider().registerWorkerMoveEventObserver(this::onMove);
        getModelEventProvider().registerWorkerBuildBlockEventObserver(this::onBuildBlock);
        getModelEventProvider().registerWorkerBuildDomeEventObserver(this::onBuildDome);
        getModelEventProvider().registerWorkerForceEventObserver(this::onForce);
        getModelEventProvider().registerPlayerLoseEventObserver(this::onLose);
        getModelEventProvider().registerPlayerWinEventObserver(this::onWin);
        // TODO: Register model events and responses

        updateView();
    }

    public GameData getData() {
        return data;
    }

    private void onPlayerTurnStart(PlayerTurnStartEvent event) {
        // The turn start event is sent before requests, the view will be updated after the requests arrive
        data = data.withTurnPlayer(event.getPlayer());
    }

    /*
     * Request events, build the GameData adding the requests
     */

    private void onRequestSelectGods(RequestPlayerChallengerSelectGodsEvent event) {
        // The gods phase does not use turn logic
        data = data.withSelectGods(new SelectGodsData(event.getGods(), event.getSelectedGodsCount()));
        updateView();
    }

    private void onRequestChooseGod(RequestPlayerChooseGodEvent event) {
        // The gods phase does not use turn logic
        data = data.withChooseGod(new ChooseGodData(event.getAvailableGods()));
        updateView();
    }

    private void onRequestSpawn(RequestWorkerSpawnEvent event) {
        // The spawn phase does not use turn logic
        data = data.withSpawn(new InteractData(event.getAvailablePositions()));
        updateView();
    }

    private void onRequestMove(RequestWorkerMoveEvent event) {
        data = data.withMove(extractData(event));
    }

    private void onRequestBuildBlock(RequestWorkerBuildBlockEvent event) {
        data = data.withBuildBlock(extractData(event));
    }

    private void onRequestBuildDome(RequestWorkerBuildDomeEvent event) {
        data = data.withBuildDome(extractData(event));
    }

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
    }

    private void onRequestEndTurn(RequestPlayerEndTurnEvent event) {
        // Called after each other request event has been called
        data = data.withEndTurn(event.getCanBeEnded());
        updateView();
    }

    /*
     * Model events, update the GameData with the new information
     */

    private void onSelectGods(PlayerChallengerSelectGodsEvent event) {
        // TODO: Show something?

        data = data.withNoRequests();
        updateView();
    }

    private void onChooseGod(PlayerChooseGodEvent event) {
        // TODO: Show something?

        if (event.getPlayer().equals(data.getName())) {
            // Leave the gods phase
            data = new GameData(null, data.getName(), data.getOtherPlayers(),
                    false, data.isSpectating(), data.getMap(), data.getWorkers())
                    .withTurnPlayer(data.getTurnPlayer().orElse(null));
        } else {
            data = data.withNoRequests();
        }

        updateView();
    }

    private void onSpawn(WorkerSpawnEvent event) {
        List<WorkerInfo> workers = new ArrayList<>(data.getWorkers());
        workers.add(new WorkerInfo(event.getId(), event.getPlayer(), event.getPosition()));

        data = new GameData(null, data.getName(), data.getOtherPlayers(),
                data.isInGodsPhase(), data.isSpectating(), data.getMap(), workers)
                .withTurnPlayer(data.getTurnPlayer().orElse(null));
        updateView();
    }

    private void onMove(WorkerMoveEvent event) {
        updatePosition(event.getId(), event.getDestination());
        updateView();
    }

    private void onBuildBlock(WorkerBuildBlockEvent event) {
        Optional<CellInfo> cell = data.getCellInfo(event.getDestination());

        if (cell.isEmpty()) {
            throw new IllegalArgumentException("Invalid cell sent by server: " + event.getDestination().toString());
        }

        CellInfo updatedCell = new CellInfo(cell.get().getLevel() + 1, cell.get().isDoomed());
        data = data.withCellInfo(event.getDestination(), updatedCell);
        updateView();
    }

    private void onBuildDome(WorkerBuildDomeEvent event) {
        Optional<CellInfo> cell = data.getCellInfo(event.getDestination());

        if (cell.isEmpty()) {
            throw new IllegalArgumentException("Invalid cell sent by server: " + event.getDestination().toString());
        }

        CellInfo updatedCell = new CellInfo(cell.get().getLevel(), true);
        data = data.withCellInfo(event.getDestination(), updatedCell);
        updateView();
    }

    private void onForce(WorkerForceEvent event) {
        Optional<CellInfo> cell = data.getCellInfo(event.getDestination());

        if (cell.isEmpty()) {
            throw new IllegalArgumentException("Invalid cell sent by server: " + event.getDestination().toString());
        }

        updatePosition(event.getTarget(), event.getDestination());
    }

    private void onLose(PlayerLoseEvent event) {
        if (!event.getPlayer().equals(data.getName())) {
            // TODO: Show players that have lost?
            return;
        }

        data = new GameData(null, data.getName(), data.getOtherPlayers(),
                data.isInGodsPhase(), true, data.getMap(), data.getWorkers());
        updateView();
    }

    private void onWin(PlayerWinEvent event) {
        getClientConnector().updateState(new EndState(getClientConnector(), data.getName(), event.getPlayer(), data.getMap(), data.getWorkers()));
    }

    /*
     * View interaction methods
     */

    public void acceptSelectGods(List<String> selectedGods) {
        data = data.withNoRequests();
        updateView();
        getClientConnector().send(new PlayerChallengerSelectGodsEvent(getData().getName(), selectedGods));
    }

    public void acceptChooseGod(String chooseGod) {
        data = data.withNoRequests();
        updateView();
        getClientConnector().send(new PlayerChooseGodEvent(getData().getName(), chooseGod));
    }

    public void acceptSpawn(int x, int y) {
        data = data.withNoRequests();
        updateView();
        getClientConnector().send(new WorkerSpawnEvent(getData().getName(), 0, new Coordinates(x, y)));
    }

    public void acceptMove(int worker, int x, int y) {
        data = data.withNoRequests();
        updateView();
        getClientConnector().send(new WorkerMoveEvent(getData().getName(), worker, new Coordinates(x, y)));
    }

    public void acceptBuildBlock(int worker, int x, int y) {
        data = data.withNoRequests();
        updateView();
        getClientConnector().send(new WorkerBuildBlockEvent(getData().getName(), worker, new Coordinates(x, y)));
    }

    public void acceptBuildDome(int worker, int x, int y) {
        data = data.withNoRequests();
        updateView();
        getClientConnector().send(new WorkerBuildDomeEvent(getData().getName(), worker, new Coordinates(x, y)));
    }

    public void acceptForce(int worker, int target, int x, int y) {
        data = data.withNoRequests();
        updateView();
        getClientConnector().send(new WorkerForceEvent(getData().getName(), worker, target, new Coordinates(x, y)));
    }

    /*
     * Utility methods
     */

    private void updateView() {
        getClientConnector().getViewer().viewGame(this);
    }

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
    }

    private WorkersInteractData extractData(AbstractRequestWorkerInteractEvent event) {
        WorkersInteractData interactData = data.getMoveData().orElse(new WorkersInteractData(Map.of()));

        Map<Integer, InteractData> interactions = new HashMap<>(interactData.getAvailableInteractions());
        interactions.put(event.getWorker(), new InteractData(event.getAvailableDestinations()));
        interactData = new WorkersInteractData(interactions);

        return interactData;
    }

}
