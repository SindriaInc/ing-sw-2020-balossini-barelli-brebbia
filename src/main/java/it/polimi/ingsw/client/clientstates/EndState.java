package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.client.data.EndData;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.WorkerInfo;

import java.util.List;

public class EndState extends AbstractClientState {

    /**
     * The data used by this state
     */
    private final EndData data;

    public EndState(ClientConnector clientConnector, String name, String winner, CellInfo[][] map, List<WorkerInfo> workers) {
        super(clientConnector);

        data = new EndData(name, winner, map, workers);
    }

    public EndData getData() {
        return data;
    }

}
