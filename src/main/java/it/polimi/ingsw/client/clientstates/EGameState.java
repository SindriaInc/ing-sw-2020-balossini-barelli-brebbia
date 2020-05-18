package it.polimi.ingsw.client.clientstates;

import it.polimi.ingsw.client.AbstractFunctions;
import it.polimi.ingsw.client.ClientData;
import it.polimi.ingsw.client.cli.CliMain;
import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.event.*;
import it.polimi.ingsw.common.event.request.RequestWorkerBuildBlockEvent;
import it.polimi.ingsw.common.event.request.RequestWorkerMoveEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EGameState extends AbstractClientState {

    private enum Phase {
        PRE_GODS,
        PRE_WORKERS,
        GAME
    }

    private AbstractFunctions usableFunctions;

    private ClientData clientData;

    private Consumer<AbstractEvent> send;

    private AbstractClientState nextState = null;

    /**
     * The list of commands that the user can type to progress in the game
     * Only available when in game
     */
    private final List<AADataTypes.Command> availableCommands = new ArrayList<>();

    /**
     * Whether or not it is this player's turn
     */
    private boolean turn = false;

    private Phase phase;

    public EGameState(AbstractFunctions usableFunctions, ClientData clientData, Consumer<AbstractEvent> send) {
        this.usableFunctions = usableFunctions;
        this.clientData = clientData;
        this.send = send;
        if (clientData.getRoom().isSimpleGame()) {
            phase = Phase.PRE_WORKERS;
        } else {
            phase = Phase.PRE_GODS;
        }
    }

    @Override
    public void onPlayerTurnStartEvent(AbstractEvent event) {
        availableCommands.clear();

        turn = ((PlayerTurnStartEvent) event).getPlayer().equals(clientData.getName());
    }

    @Override
    public void onRequestPlayerChallengerSelectGodsEvent(AbstractEvent event) {
        //TODO
    }

    @Override
    public void onRequestPlayerChooseGodEvent(AbstractEvent event) {
        //TODO
    }

    @Override
    public void onRequestWorkerSpawnEvent(AbstractEvent event) {
        availableCommands.add(new AADataTypes.Command("spawn", "x:y",
                args -> {
                    if (args.length != 2) {
                        return false;
                    }

                    Coordinates coordinates = coordinatesOrNull(args);

                    if (coordinates == null) {
                        return false;
                    }

                    send.accept(new WorkerSpawnEvent(clientData.getName(), -1, coordinates));
                    return true;
                }
        ));
    }

    @Override
    public void onRequestWorkerMoveEvent(AbstractEvent event) {
        int worker = ((RequestWorkerMoveEvent) event).getWorker();

        availableCommands.add(new AADataTypes.Command("move" + worker, "x:y",
                args -> {
                    if (args.length != 2) {
                        return false;
                    }

                    Coordinates coordinates = coordinatesOrNull(args);

                    if (coordinates == null) {
                        return false;
                    }

                    send.accept(new WorkerMoveEvent(clientData.getName(), worker, coordinates));
                    return true;
                }
        ));
    }

    @Override
    public void onRequestWorkerBuildBlockEvent(AbstractEvent event) {
        int worker = ((RequestWorkerBuildBlockEvent) event).getWorker();

        availableCommands.add(new AADataTypes.Command("block" + worker, "x:y",
                args -> {
                    if (args.length != 2) {
                        return false;
                    }

                    Coordinates coordinates = coordinatesOrNull(args);

                    if (coordinates == null) {
                        return false;
                    }

                    send.accept(new WorkerBuildBlockEvent(clientData.getName(), worker, coordinates));
                    return true;
                }
        ));
    }

    @Override
    public void onRequestWorkerBuildDomeEvent(AbstractEvent event) {
        int worker = ((RequestWorkerBuildBlockEvent) event).getWorker();

        availableCommands.add(new AADataTypes.Command("dome" + worker, "x:y",
                args -> {
                    if (args.length != 2) {
                        return false;
                    }

                    Coordinates coordinates = coordinatesOrNull(args);

                    if (coordinates == null) {
                        return false;
                    }

                    send.accept(new WorkerBuildDomeEvent(clientData.getName(), worker, coordinates));
                    return true;
                }
        ));
    }

    @Override
    public void onRequestPlayerEndTurnEvent(AbstractEvent event) {
        availableCommands.add(new AADataTypes.Command("end", null,
                args -> {
                    if (args.length != 0) {
                        return false;
                    }

                    send.accept(new PlayerEndTurnEvent(clientData.getName()));
                    return true;
                }
        ));
    }

    @Override
    public void onPlayerChallengerSelectGodsEvent(AbstractEvent event) {
        //TODO
    }

    @Override
    public void onPlayerChooseGodEvent(AbstractEvent event) {
        //TODO
    }

    @Override
    public void onWorkerSpawnEvent(AbstractEvent event) {
        WorkerSpawnEvent workerSpawnEvent = (WorkerSpawnEvent) event;

        clientData.getWorkers().put(workerSpawnEvent.getId(), new AADataTypes.WorkerInfo(workerSpawnEvent.getId(), workerSpawnEvent.getPlayer(), workerSpawnEvent.getPosition()));
        usableFunctions.printBoard(clientData, AADataTypes.InGame.IN_GAME, availableCommands);
    }

    @Override
    public void onWorkerMoveEvent(AbstractEvent event) {
        WorkerMoveEvent workerMoveEvent = (WorkerMoveEvent) event;

        clientData.getWorkers().get(workerMoveEvent.getId()).setPosition(((WorkerMoveEvent) event).getDestination());
        usableFunctions.printBoard(clientData, AADataTypes.InGame.IN_GAME, availableCommands);
    }

    @Override
    public void onWorkerBuildBlockEvent(AbstractEvent event) {
        WorkerBuildBlockEvent workerBuildBlockEvent = (WorkerBuildBlockEvent) event;

        AADataTypes.CellInfo cellInfo = getCellInfo(workerBuildBlockEvent.getDestination());
        cellInfo.setLevel(cellInfo.getLevel() + 1);
        usableFunctions.printBoard(clientData, AADataTypes.InGame.IN_GAME, availableCommands);
    }

    @Override
    public void onWorkerBuildDomeEvent(AbstractEvent event) {
        WorkerBuildDomeEvent workerBuildDomeEvent = (WorkerBuildDomeEvent) event;

        AADataTypes.CellInfo cellInfo = getCellInfo(workerBuildDomeEvent.getDestination());
        cellInfo.setDoomed(true);
        usableFunctions.printBoard(clientData, AADataTypes.InGame.IN_GAME, availableCommands);
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

        if (beatenPlayer.equals(clientData.getName())) {
            nextState = new FSpectatorState(usableFunctions, clientData, send);
            usableFunctions.printBoard(clientData, AADataTypes.InGame.SPECTATOR, null);
        } else {
            usableFunctions.printBoard(clientData, AADataTypes.InGame.IN_GAME, availableCommands);
        }
    }

    @Override
    public AbstractClientState nextClientState() {
        if (nextState==null) {
            return this;
        }
        return nextState;
    }

    @Override
    public AADataTypes.Response onCommand(String message) {

        String command = usableFunctions.in("Insert command: ");
        String[] parameters = command.split(":");

        if (parameters.length == 0) {
            return new AADataTypes.Response(false, "Too few parameters");
        }

        String label = parameters[0];
        String[] args = Arrays.copyOfRange(parameters, 1, parameters.length);

        for (AADataTypes.Command availableCommand : availableCommands) {
            if (!label.equals(availableCommand.getLabel())) {
                return new AADataTypes.Response(true, null);
            } else {
                return new AADataTypes.Response(false, "Invalid parameters");
            }
        }

        return new AADataTypes.Response(false, "Not your turn");

    }

    private Coordinates coordinatesOrNull(String[] args) {
        Integer x = intOrNull(args[0]);
        Integer y = intOrNull(args[1]);

        if (x == null || y == null) {
            return null;
        }

        return new Coordinates(x, y);
    }

    private Integer intOrNull(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private AADataTypes.CellInfo getCellInfo(Coordinates coordinates) {
        return clientData.getMap()[coordinates.getX()][coordinates.getY()];
    }

}
