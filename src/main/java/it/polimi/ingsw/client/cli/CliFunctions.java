package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.AbstractFunctions;
import it.polimi.ingsw.client.ClientData;
import it.polimi.ingsw.client.clientstates.AADataTypes;
import it.polimi.ingsw.common.Coordinates;
import it.polimi.ingsw.common.RoomInfo;
import it.polimi.ingsw.common.logging.Logger;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import static it.polimi.ingsw.client.cli.CliConstants.*;

public class CliFunctions extends AbstractFunctions {

    private boolean active;

    private final BlockingDeque<String> pendingMessages = new LinkedBlockingDeque<>();

    private InputHandler inputHandler;

    private ExecutorService executorService;

    /**
     * The lines to be sent to the logger when flushing
     */
    private final List<String> output = new ArrayList<>();

    public CliFunctions(InputHandler inputHandler) {
        this.active = true;

        executorService = Executors.newCachedThreadPool();
        executorService.submit(this::readMessages);
    }

    private void readMessages() {
        while (active) {
            try {
                String pendingMessage = pendingMessages.take();
            } catch (InterruptedException e) {

            }
        }
    }

    @Override
    public String in(String request) {
        return null;
    }

    @Override
    public int inInt(String request) {
        return 0;
    }

    @Override
    public void out(String message) {
        output.add(message);
    }

    @Override
    public void printLobby(ClientData clientData) {
        out(CLEAR);
        out("> Logged in as " + clientData.getName());
        out(SEPARATOR_HIGH);
        for (RoomInfo roomInfo : clientData.getRooms()) {
            int count = clientData.getRoomCount(roomInfo);
            int max = roomInfo.getMaxPlayers();
            String simple = roomInfo.isSimpleGame() ? "Yes" : "No";

            out("# Name: " + roomInfo.getOwner() + " (Players: " + count + "/" + max + ", Simple: " + simple + ")");
            out(SEPARATOR_LOW);
        }
        out("Type \"" + CliConstants.COMMAND_CREATE +":max-players:simple|normal\" to create a new room, or type \"" + COMMAND_JOIN + ":room-name\" to join a room");
        flush();
    }

    @Override
    public void printRoom(ClientData clientData) {
        RoomInfo room = clientData.getRoom();

        int count = getRoomCount(room);
        int max = room.getMaxPlayers();

        out(CLEAR);
        out(SEPARATOR_HIGH);
        out("You are in the room: " + room.getOwner());
        out("Players (" + count +"/" + max + "): " + String.join(", ", room.getOtherPlayers()));
        out("The game will start as soon as the room is filled");
        out(SEPARATOR_HIGH);
        flush();
    }

    private static final String ROOF =      "    ______ ______ ______ ______ ______";
    private static final String FIRST_ROW = "   |      |      |      |      |      |";
    private static final String LAST_ROW =  "   |______|______|______|______|______|";

    private String getBlocksChar (int n) {
        if (n==3)
            return "⦀";
        if (n==2)
            return "‖";
        if (n==1)
            return "|";
        return " ";
    }

    @Override
    public void printBoard(ClientData clientData, AADataTypes.InGame inGame, List<AADataTypes.Command> availableCommands) {
        out(CLEAR);
        out(SEPARATOR_HIGH);
        out(ROOF);

        StringBuilder boardBuilder = new StringBuilder();
        // Scan top to bottom to easily print in the correct order
        for (int y = Game.BOARD_ROWS - 1; y >= 0; y--) {

            boardBuilder.append(FIRST_ROW).append(System.lineSeparator());
            boardBuilder.append("   | ");
            for (int x = 0; x < Game.BOARD_COLUMNS; x++) {
                AADataTypes.CellInfo cellInfo = getCellInfo(clientData, new Coordinates(x, y));
                AADataTypes.WorkerInfo workerInfo = getWorkerByCoords(clientData, x, y);

                boardBuilder.append(getBlocksChar(cellInfo.getLevel()));
                if (cellInfo.isDoomed()) {
                    boardBuilder.append("()");
                } else if (workerInfo != null) {
                    boardBuilder.append("#" + workerInfo.getId());
                } else {
                    boardBuilder.append("  ");
                }
                boardBuilder.append(getBlocksChar(cellInfo.getLevel()));
                boardBuilder.append(" | ");
            }

            if (y == 3) {
                boardBuilder.append(" ".repeat(SPACING * 2)).append("Your workers: ");
            }

            if (y == 2) {
                List<String> workerIds = new ArrayList<>();
                for (AADataTypes.WorkerInfo workerInfo : clientData.getWorkers().values()) {
                    if (workerInfo.getOwner().equals(clientData.getName())) {
                        workerIds.add(workerInfo.getId() + "");
                    }
                }

                boardBuilder.append(" ".repeat(SPACING * 2));

                if (workerIds.size() == 0) {
                    boardBuilder.append("No workers spawned");
                } else {
                    boardBuilder.append(String.join(", ", workerIds));
                }
            }

            boardBuilder.append(" ".repeat(SPACING * Game.BOARD_COLUMNS)).append(System.lineSeparator());
            boardBuilder.append(LAST_ROW).append(System.lineSeparator());
        }
        boardBuilder.append(SEPARATOR_HIGH).append(System.lineSeparator());

        if (inGame == AADataTypes.InGame.IN_GAME) {
            if (availableCommands!=null && availableCommands.size() > 0) {
                boardBuilder.append("# Available commands:");
                for (AADataTypes.Command command : availableCommands) {
                    boardBuilder.append(System.lineSeparator()).append(command.getSyntax());
                }
            } else {
                boardBuilder.append("# Waiting for your turn...");
            }

            out(boardBuilder.toString());
            flush();
        } else if (inGame == AADataTypes.InGame.SPECTATOR) {
            boardBuilder.append("# You have lost the game and are now spectating");

            out(boardBuilder.toString());
            flush();
        } else if (inGame == AADataTypes.InGame.ENDED) {
            out(boardBuilder.toString());
        }
    }

    public void printEnd (ClientData clientData) {
        printBoard(clientData, AADataTypes.InGame.ENDED, null);
        if (clientData.getWinner().equals(clientData.getName())) {
            out(" ".repeat(SPACING) + "Congratulations! You have won the game!");
        } else {
            out(" ".repeat(SPACING) + clientData.getWinner() + " has won the game");
        }
        out(SEPARATOR_HIGH);
        flush();
    }

    private void flush() {
        if (output.size() <= 0) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(output.get(0));
        for (String line : output.subList(1, output.size())) {
            builder.append(System.lineSeparator());
            builder.append(line);
        }
        output.clear();

        Logger.getInstance().info(builder.toString());
    }

    private int getRoomCount(RoomInfo info) {
        return info.getOtherPlayers().size() + 1;
    }

    private AADataTypes.CellInfo getCellInfo(ClientData clientData, Coordinates coordinates) {
        return clientData.getMap()[coordinates.getX()][coordinates.getY()];
    }

    private AADataTypes.WorkerInfo getWorkerByCoords(ClientData clientData, int x, int y) {
        for (AADataTypes.WorkerInfo other : clientData.getWorkers().values()) {
            if (other.getPosition().equals(new Coordinates(x, y))) {
                return other;
            }
        }

        return null;
    }

}
