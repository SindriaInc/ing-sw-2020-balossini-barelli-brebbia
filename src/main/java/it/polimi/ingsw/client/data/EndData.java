package it.polimi.ingsw.client.data;

import it.polimi.ingsw.client.data.request.*;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.Coordinates;
import it.polimi.ingsw.common.info.WorkerInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EndData extends AbstractData {

    private final String name;
    private final String winner;

    private final CellInfo[][] map;
    private final List<WorkerInfo> workers;

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

    private CellInfo[][] copyOfMap() {
        return Arrays.stream(this.map).map(CellInfo[]::clone).toArray(CellInfo[][]::new);
    }

}
