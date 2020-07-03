package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.data.request.*;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.info.WorkerInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represent the data of the end state of the game
 */
public class EndData extends AbstractData {

    /**
     * The player's name
     */
    private final String name;

    /**
     * The winner's name
     */
    private final String winner;

    /**
     * The map
     */
    private final CellInfo[][] map;

    /**
     * The worker list
     */
    private final List<WorkerInfo> workers;

    /**
     * Class constructor, set player's name, winner's name, map and worker list
     *
     * @param name The player's name
     * @param winner The winner's name
     * @param map The map
     * @param workers The workers
     */
    public EndData(String name, String winner, CellInfo[][] map, List<WorkerInfo> workers) {
        // The end state does not accept any command
        super(null);

        this.name = name;
        this.winner = winner;
        this.map = map;
        this.workers = workers;
    }

    public String getName() {
        return name;
    }

    public String getWinner() {
        return winner;
    }

    public CellInfo[][] getMap() {
        return copyOfMap();
    }

    public List<WorkerInfo> getWorkers() {
        return workers;
    }

    /**
     * Create a map's copy
     * @return The map's copy
     */
    private CellInfo[][] copyOfMap() {
        return Arrays.stream(this.map).map(CellInfo[]::clone).toArray(CellInfo[][]::new);
    }

}
