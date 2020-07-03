package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.ClientConnector;
import it.polimi.ingsw.client.data.EndData;
import it.polimi.ingsw.common.info.CellInfo;
import it.polimi.ingsw.common.info.WorkerInfo;

import java.util.List;

/**
 * Generate the data for the end phase of the game
 */
public class EndState extends AbstractClientState {

    /**
     * The data used by this state
     */
    private final EndData data;

    /**
     * Class constructor, set client connector.
     * Generate the end data given player name, winner name, map and workers
     *
     * @param clientConnector The client connector
     * @param name The player name
     * @param winner The winner
     * @param map The map
     * @param workers The workers
     */
    public EndState(ClientConnector clientConnector, String name, String winner, CellInfo[][] map, List<WorkerInfo> workers) {
        super(clientConnector);

        data = new EndData(name, winner, map, workers);
        updateView();
    }

    public EndData getData() {
        return data;
    }

    /**
     * Update the view
     */
    private void updateView() {
        getClientConnector().getViewer().viewEnd(this);
    }

}
