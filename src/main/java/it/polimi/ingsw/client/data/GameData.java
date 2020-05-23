package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.data.request.*;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.info.WorkerInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameData extends AbstractData {

    private final String name;
    private final List<String> otherPlayers;

    private final boolean inGodsPhase;
    private final boolean spectating;
    private final CellInfo[][] map;
    private final List<WorkerInfo> workers;

    private final String turnPlayer;
    private final SelectGodsData selectGodsData;
    private final ChooseGodData chooseGodData;
    private final InteractData spawnData;
    private final WorkersInteractData moveData;
    private final WorkersInteractData buildBlockData;
    private final WorkersInteractData buildDomeData;
    private final WorkersOtherInteractData forceData;
    private final Boolean canBeEnded;

    public GameData(String lastMessage, String name, List<String> otherPlayers, boolean inGodsPhase, boolean spectating,
                    CellInfo[][] map, List<WorkerInfo> workers) {
        this(lastMessage, name, otherPlayers, inGodsPhase, spectating, map, workers, null,
                null, null,
                null, null,
                null, null,
                null, null);
    }

    private GameData(String lastMessage, String name, List<String> otherPlayers, boolean inGodsPhase, boolean spectating,
                     CellInfo[][] map, List<WorkerInfo> workers, String turnPlayer,
                     SelectGodsData selectGodsData, ChooseGodData chooseGodData,
                     InteractData spawnData, WorkersInteractData moveData,
                     WorkersInteractData buildBlockData, WorkersInteractData buildDomeData,
                     WorkersOtherInteractData forceData, Boolean canBeEnded) {
        super(lastMessage);

        this.name = name;
        this.otherPlayers = otherPlayers;
        this.inGodsPhase = inGodsPhase;
        this.spectating = spectating;
        this.map = map;
        this.workers = workers;
        this.turnPlayer = turnPlayer;
        this.selectGodsData = selectGodsData;
        this.chooseGodData = chooseGodData;
        this.spawnData = spawnData;
        this.moveData = moveData;
        this.buildBlockData = buildBlockData;
        this.buildDomeData = buildDomeData;
        this.forceData = forceData;
        this.canBeEnded = canBeEnded;
    }

    public String getName() {
        return name;
    }

    public List<String> getOtherPlayers() {
        return List.copyOf(otherPlayers);
    }

    public boolean isInGodsPhase() {
        return inGodsPhase;
    }

    public boolean isSpectating() {
        return spectating;
    }

    public CellInfo[][] getMap() {
        return copyOfMap();
    }

    public List<WorkerInfo> getWorkers() {
        return workers;
    }

    public Optional<String> getTurnPlayer() {
        return Optional.ofNullable(turnPlayer);
    }

    public Optional<SelectGodsData> getSelectGodsData() {
        return Optional.ofNullable(selectGodsData);
    }

    public Optional<ChooseGodData> getChooseGodData() {
        return Optional.ofNullable(chooseGodData);
    }

    public Optional<WorkersInteractData> getMoveData() {
        return Optional.ofNullable(moveData);
    }

    public Optional<WorkersInteractData> getBuildBlockData() {
        return Optional.ofNullable(buildBlockData);
    }

    public Optional<WorkersInteractData> getBuildDomeData() {
        return Optional.ofNullable(buildDomeData);
    }

    public Optional<WorkersOtherInteractData> getForceData() {
        return Optional.ofNullable(forceData);
    }

    public Optional<Boolean> getCanBeEnded() {
        return Optional.ofNullable(canBeEnded);
    }

    public Optional<CellInfo> getCellInfo(Coordinates coordinates) {
        return getCellInfo(coordinates.getX(), coordinates.getY());
    }

    public Optional<CellInfo> getCellInfo(int x, int y) {
        if (x >= map.length) {
            return Optional.empty();
        }

        CellInfo[] column = map[x];

        if (y >= column.length) {
            return Optional.empty();
        }

        return Optional.of(column[y]);
    }

    public GameData withCellInfo(Coordinates coordinates, CellInfo cellInfo) {
        if (getCellInfo(coordinates).isEmpty()) {
            throw new IllegalArgumentException("Invalid coordinates: " + coordinates.toString());
        }

        CellInfo[][] map = copyOfMap();
        map[coordinates.getX()][coordinates.getY()] = cellInfo;

        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    public GameData withTurnPlayer(String turnPlayer) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    public GameData withSelectGods(SelectGodsData selectGodsData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    public GameData withChooseGod(ChooseGodData chooseGodData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    public GameData withSpawn(InteractData spawnData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    public GameData withMove(WorkersInteractData moveData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    public GameData withBuildBlock(WorkersInteractData buildBlockData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    public GameData withBuildDome(WorkersInteractData buildDomeData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    public GameData withForce(WorkersOtherInteractData forceData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    public GameData withEndTurn(Boolean canBeEnded) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    public GameData withNoRequests() {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                null, null,
                null, null,
                null, null,
                null, null
        );
    }

    private CellInfo[][] copyOfMap() {
        return Arrays.stream(this.map).map(CellInfo[]::clone).toArray(CellInfo[][]::new);
    }

}
