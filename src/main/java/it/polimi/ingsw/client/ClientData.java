package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CliMain;
import it.polimi.ingsw.client.clientstates.AADataTypes;
import it.polimi.ingsw.client.clientstates.AbstractClientState;
import it.polimi.ingsw.common.RoomInfo;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientData {

    /**
     * Player name
     */
    private String name;

    /**
     * Player age
     */
    private int age;

    /**
     * Server IP address
     */
    private String serverIP;

    /**
     * Server port
     */
    private int serverPort;

    /**
     * Whether or not the last event was rejected by the server
     */
    private boolean invalid = false;

    /**
     * The list of rooms
     * Only available when in the lobby
     */
    private final List<RoomInfo> rooms = new ArrayList<>();

    /**
     * The chosen room
     */
    private RoomInfo room;

    /**
     * The game map
     * Only available when in game
     */
    private AADataTypes.CellInfo[][] map = new AADataTypes.CellInfo[Game.BOARD_COLUMNS][Game.BOARD_ROWS];

    /**
     * The winner
     */
    private String winner = null;

    /**
     * The workers
     * Only available when in game
     */
    private final HashMap<Integer, AADataTypes.WorkerInfo> workers = new HashMap<>();

    /**
     * The client instance, implementing the connection to the server
     */
    private IClient client;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<RoomInfo> getRooms() {
        return rooms;
    }

    public int getRoomCount(RoomInfo info) {
        return info.getOtherPlayers().size() + 1;
    }

    public RoomInfo getRoom() {
        return room;
    }

    public void setRoom(RoomInfo room) {
        this.room = room;
    }

    public AADataTypes.CellInfo[][] getMap() {
        return map;
    }

    public HashMap<Integer, AADataTypes.WorkerInfo> getWorkers() {
        return workers;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public IClient getClient() {
        return client;
    }

    public void setClient(IClient client) {
        this.client = client;
    }
}
