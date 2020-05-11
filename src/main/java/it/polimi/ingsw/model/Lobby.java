package it.polimi.ingsw.model;

import it.polimi.ingsw.common.RoomInfo;
import it.polimi.ingsw.common.event.lobby.LobbyGameStartEvent;
import it.polimi.ingsw.common.event.lobby.LobbyRoomUpdateEvent;
import it.polimi.ingsw.common.event.lobby.LobbyUpdateEvent;
import it.polimi.ingsw.common.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Lobby {

    private static final int MAX_ROOM_PLAYERS = 4;

    private final ModelEventProvider provider;

    private final List<Player> freePlayers = new ArrayList<>();

    private final List<Room> rooms = new ArrayList<>();

    private final List<Game> games = new ArrayList<>();

    public Lobby() {
        this.provider = new ModelEventProvider();
    }

    public ModelEventProvider getModelEventProvider() {
        return provider;
    }

    public ModelResponse login(String player, int age) {
        if (isFree(player) || isInRoom(player) || isInGame(player)) {
            Logger.getInstance().debug("Received login with an invalid player");
            return ModelResponse.INVALID_PARAMS;
        }

        if (age <= 0) {
            Logger.getInstance().debug("Received login with an invalid age");
            return ModelResponse.INVALID_PARAMS;
        }

        freePlayers.add(new Player(player, age));
        provider.getLobbyUpdateEventObservable().notifyObservers(new LobbyUpdateEvent(player, generateRoomInfos()));
        return ModelResponse.ALLOW;
    }

    public ModelResponse createRoom(String player, int maxPlayers, boolean simpleGame) {
        Optional<Player> foundPlayer = getFreePlayer(player);

        if (foundPlayer.isEmpty()) {
            // Trying to create a room while already in a room
            return ModelResponse.INVALID_STATE;
        }

        if (maxPlayers <= 1 || maxPlayers > MAX_ROOM_PLAYERS) {
            // Trying to create a room with an invalid player limit
            return ModelResponse.INVALID_STATE;
        }

        Room room = new Room(foundPlayer.get(), maxPlayers, simpleGame);
        rooms.add(room);
        notifyRoomUpdate(room);
        return ModelResponse.ALLOW;
    }

    public ModelResponse joinRoom(String player, String owner) {
        Optional<Player> foundPlayer = getFreePlayer(player);

        if (foundPlayer.isEmpty()) {
            // Trying to join a room while already in a room
            return ModelResponse.INVALID_STATE;
        }

        Optional<Room> foundRoom = getRoomByOwner(owner);

        if (foundRoom.isEmpty()) {
            // Trying to join a non-existing room
            return ModelResponse.INVALID_PARAMS;
        }

        Room room = foundRoom.get();

        if (room.isFull()) {
            // Trying to join a full room
            return ModelResponse.INVALID_PARAMS;
        }

        room.addPlayer(foundPlayer.get());
        freePlayers.remove(foundPlayer.get());

        if (room.isFull()) {
            startGame(room);
            return ModelResponse.ALLOW;
        }

        notifyLobbyUpdate();
        return ModelResponse.ALLOW;
    }

    public Optional<Game> getGame(String player) {
        return games.stream().filter(game -> game.getAllPlayers().stream().anyMatch(other -> other.getName().equals(player))).findFirst();
    }

    private void startGame(Room room) {
        rooms.remove(room);
        notifyGameStart(room);

        Game game = new Game(provider);
        game.init(room.getAllPlayers(), new Deck(List.of()), room.isSimpleGame());
        games.add(game);
    }

    private void notifyGameStart(Room room) {
        RoomInfo roomInfo = generateRoomInfo(room);

        for (Player other : room.getAllPlayers()) {
            provider.getLobbyGameStartEventObservable().notifyObservers(
                    new LobbyGameStartEvent(other.getName(), roomInfo)
            );
        }
    }

    private void notifyRoomUpdate(Room room) {
        RoomInfo roomInfo = generateRoomInfo(room);

        for (Player other : room.getAllPlayers()) {
            provider.getLobbyRoomUpdateEventObservable().notifyObservers(
                    new LobbyRoomUpdateEvent(other.getName(), roomInfo)
            );
        }
    }

    private void notifyLobbyUpdate() {
        List<RoomInfo> roomInfos = generateRoomInfos();

        for (Player player : freePlayers) {
            provider.getLobbyUpdateEventObservable().notifyObservers(
                    new LobbyUpdateEvent(player.getName(), roomInfos)
            );
        }
    }

    private Optional<Player> getFreePlayer(String name) {
        return freePlayers.stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    private Optional<Room> getRoomByOwner(String owner) {
        return rooms.stream().filter(room -> room.getOwner().getName().equals(owner)).findFirst();
    }

    private List<RoomInfo> generateRoomInfos() {
        List<RoomInfo> roomInfos = new ArrayList<>();

        for (Room room : rooms) {
            roomInfos.add(generateRoomInfo(room));
        }

        return roomInfos;
    }

    private RoomInfo generateRoomInfo(Room room) {
        return new RoomInfo(
                room.getOwner().getName(),
                room.getOtherPlayers().stream().map(Player::getName).collect(Collectors.toList()),
                room.getMaxPlayers(),
                room.isSimpleGame());
    }

    private boolean isFree(String name) {
        return getFreePlayer(name).isPresent();
    }

    private boolean isInRoom(String name) {
        return rooms.stream().anyMatch(
                room -> room.getOwner().getName().equals(name) ||
                room.getOtherPlayers().stream().anyMatch(other -> other.getName().equals(name))
        );
    }

    private boolean isInGame(String name) {
        return games.stream().anyMatch(
                game -> game.getAllPlayers().stream().anyMatch(other -> other.getName().equals(name))
        );
    }

}
