package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.data.request.*;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.info.WorkerInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represent the data of the game state
 */
public class GameData extends AbstractData {

    /**
     * The player's name
     */
    private final String name;

    /**
     * The other player's list
     */
    private final List<String> otherPlayers;

    /**
     * If the game is in gods phase
     */
    private final boolean inGodsPhase;

    /**
     * If the player is spectating
     */
    private final boolean spectating;

    /**
     * The map
     */
    private final CellInfo[][] map;

    /**
     * The list of worker
     */
    private final List<WorkerInfo> workers;

    /**
     * The player on turn
     */
    private final String turnPlayer;

    /**
     * The select god data
     */
    private final SelectGodsData selectGodsData;

    /**
     * The choose god data
     */
    private final ChooseGodData chooseGodData;

    /**
     * The select first data
     */
    private final SelectFirstData selectFirstData;

    /**
     * The spawn data
     */
    private final InteractData spawnData;

    /**
     * The move data
     */
    private final WorkersInteractData moveData;

    /**
     * The build block data
     */
    private final WorkersInteractData buildBlockData;

    /**
     * The build dome data
     */
    private final WorkersInteractData buildDomeData;

    /**
     * THe force data
     */
    private final WorkersOtherInteractData forceData;

    /**
     * If the turn can be ended
     */
    private final Boolean canBeEnded;

    /**
     * Class constructor, set last message, name, othe players list, phase of the game state, spectating state, map and workers list
     *
     * @param lastMessage The last message
     * @param name The name
     * @param otherPlayers The other player's list
     * @param inGodsPhase Whether or not the game is in god phase
     * @param spectating Whether or not the player is spectating
     * @param map The map
     * @param workers The worker list
     */
    public GameData(String lastMessage, String name, List<String> otherPlayers, boolean inGodsPhase, boolean spectating,
                    CellInfo[][] map, List<WorkerInfo> workers) {
        this(lastMessage, name, otherPlayers, inGodsPhase, spectating, map, workers, null,
                null, null, null,
                null, null,
                null, null,
                null, null);
    }

    /**
     * Class constructor, sets all the class attributes
     *
     * @param lastMessage The last message
     * @param name The player's name
     * @param otherPlayers The other players list
     * @param inGodsPhase Whether the game is in god phase or not
     * @param spectating Whether the player is spectating or not
     * @param map The map
     * @param workers The worker list
     * @param turnPlayer The player on turn
     * @param selectGodsData The data for gods selection
     * @param chooseGodData The data for gods choosing
     * @param selectFirstData The data for first player selection
     * @param spawnData The spawn data
     * @param moveData The move data
     * @param buildBlockData The build block data
     * @param buildDomeData The build dome data
     * @param forceData The force data
     * @param canBeEnded Whether the turn can be ended or not
     */
    private GameData(String lastMessage, String name, List<String> otherPlayers, boolean inGodsPhase, boolean spectating,
                     CellInfo[][] map, List<WorkerInfo> workers, String turnPlayer,
                     SelectGodsData selectGodsData, ChooseGodData chooseGodData, SelectFirstData selectFirstData,
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
        this.selectFirstData = selectFirstData;
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

    public Optional<SelectFirstData> getSelectFirstData() {
        return Optional.ofNullable(selectFirstData);
    }

    public Optional<InteractData> getSpawnData() {
        return Optional.ofNullable(spawnData);
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

    /**
     * Get a cell info
     * @param x The x index
     * @param y The y index
     * @return The cell
     */
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

    /**
     * Create a new GameData given a last message
     * @param lastMessage The last message
     * @return The new GameData
     */
    public GameData withMessage(String lastMessage) {
        return new GameData(lastMessage, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData given a map with an updated cell
     * @param coordinates The cell coordinates
     * @param cellInfo The new cell info
     * @return The new GameData
     */
    public GameData withCellInfo(Coordinates coordinates, CellInfo cellInfo) {
        if (getCellInfo(coordinates).isEmpty()) {
            throw new IllegalArgumentException("Invalid coordinates: " + coordinates.toString());
        }

        CellInfo[][] map = copyOfMap();
        map[coordinates.getX()][coordinates.getY()] = cellInfo;

        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData given the player on turn
     * @param turnPlayer The player
     * @return The new GameData
     */
    public GameData withTurnPlayer(String turnPlayer) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData with select gods data
     * @param selectGodsData The selected gods
     * @return The new GameData
     */
    public GameData withSelectGods(SelectGodsData selectGodsData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData given choose god data
     * @param chooseGodData The chosen god
     * @return The new GameData
     */
    public GameData withChooseGod(ChooseGodData chooseGodData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData given select first data
     * @param selectFirstData The first to play
     * @return The new GameData
     */
    public GameData withSelectFirst(SelectFirstData selectFirstData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData given spawn data
     * @param spawnData The spawn data
     * @return The GameData
     */
    public GameData withSpawn(InteractData spawnData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData given move data
     * @param moveData The move data
     * @return The new GameData
     */
    public GameData withMove(WorkersInteractData moveData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData given a build block data
     * @param buildBlockData The build block data
     * @return The new GameData
     */
    public GameData withBuildBlock(WorkersInteractData buildBlockData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData given a build dome data
     * @param buildDomeData The build dome data
     * @return The new GameData
     */
    public GameData withBuildDome(WorkersInteractData buildDomeData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData given the force data
     * @param forceData The force data
     * @return The new GameData
     */
    public GameData withForce(WorkersOtherInteractData forceData) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData stated if the turn can be ended
     * @param canBeEnded If the turn can be ended
     * @return The new GameData
     */
    public GameData withEndTurn(Boolean canBeEnded) {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                selectGodsData, chooseGodData, selectFirstData,
                spawnData, moveData,
                buildBlockData, buildDomeData,
                forceData, canBeEnded
        );
    }

    /**
     * Create a new GameData mantaining name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer
     * @return The new GameData
     */
    public GameData withNoRequests() {
        return new GameData(null, name, otherPlayers, inGodsPhase, spectating, map, workers, turnPlayer,
                null, null, null,
                null, null,
                null, null,
                null, null
        );
    }

    /**
     * Create a map's copy
     * @return The map copy
     */
    private CellInfo[][] copyOfMap() {
        return Arrays.stream(this.map).map(CellInfo[]::clone).toArray(CellInfo[][]::new);
    }

}
