package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.AbstractFunctions;
import it.polimi.ingsw.client.ClientData;
import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.event.*;

import java.util.function.Consumer;

public class FSpectatorState extends AbstractClientState {

    private AbstractFunctions usableFunctions;

    private ClientData clientData;

    private Consumer<AbstractEvent> send;

    private  AbstractClientState nextState = null;

    public FSpectatorState(AbstractFunctions usableFunctions, ClientData clientData, Consumer<AbstractEvent> send) {
        this.usableFunctions = usableFunctions;
        this.clientData = clientData;
        this.send = send;
    }

    @Override
    public AbstractClientState nextClientState() {
        if (nextState == null) {
            return this;
        }
        return nextState;
    }

    @Override
    public void onWorkerMoveEvent(AbstractEvent event) {
        WorkerMoveEvent workerMoveEvent = (WorkerMoveEvent) event;

        clientData.getWorkers().get(workerMoveEvent.getId()).setPosition(((WorkerMoveEvent) event).getDestination());
        usableFunctions.printBoard(clientData, AADataTypes.InGame.SPECTATOR, null);
    }

    @Override
    public void onWorkerBuildBlockEvent(AbstractEvent event) {
        WorkerBuildBlockEvent workerBuildBlockEvent = (WorkerBuildBlockEvent) event;

        AADataTypes.CellInfo cellInfo = getCellInfo(workerBuildBlockEvent.getDestination());
        cellInfo.setLevel(cellInfo.getLevel() + 1);
        usableFunctions.printBoard(clientData, AADataTypes.InGame.SPECTATOR, null);
    }

    @Override
    public void onWorkerBuildDomeEvent(AbstractEvent event) {
        WorkerBuildDomeEvent workerBuildDomeEvent = (WorkerBuildDomeEvent) event;

        AADataTypes.CellInfo cellInfo = getCellInfo(workerBuildDomeEvent.getDestination());
        cellInfo.setDoomed(true);
        usableFunctions.printBoard(clientData, AADataTypes.InGame.SPECTATOR, null);
    }

    @Override
    public void onPlayerWinEvent(AbstractEvent event) {
        PlayerWinEvent playerWinEvent = (PlayerWinEvent) event;

        clientData.setWinner(playerWinEvent.getPlayer());
        nextState = new GEndState(usableFunctions, clientData, send);
        usableFunctions.printEnd(clientData);
    }

    @Override
    public void onPlayerLoseEvent(AbstractEvent event) {
        String beatenPlayer = ((PlayerLoseEvent) event).getPlayer();

        for (AADataTypes.WorkerInfo worker : clientData.getWorkers().values()) {
            if (beatenPlayer.equals(worker.getOwner())) {
                clientData.getWorkers().remove(worker);
            }
        }

        usableFunctions.printBoard(clientData, AADataTypes.InGame.SPECTATOR, null);
    }

    @Override
    public AADataTypes.Response onCommand(String message) {
        return null;
    }

    private AADataTypes.CellInfo getCellInfo(Coordinates coordinates) {
        return clientData.getMap()[coordinates.getX()][coordinates.getY()];
    }

}
