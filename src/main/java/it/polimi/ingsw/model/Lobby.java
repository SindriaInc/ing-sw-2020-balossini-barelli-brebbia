package it.polimi.ingsw.model;

import it.polimi.ingsw.common.IPlayerChecker;
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

    private final Deck deck;

    public Lobby(Deck deck) {
        this.provider = new ModelEventProvider();
        this.deck = deck;
    }

    public ModelEventProvider getModelEventProvider() {
        return provider;
    }

    public IPlayerChecker getPlayerChecker() {
        return this::isAvailable;
    }

    /**
     * Handle the player login
     * @param player The player that wants to login
     * @param age The age of the player, assumed to be valid
     * @return the response - ModelResponse.ALLOW if the name is available
     */
    public ModelResponse login(String player, int age) {
        if (!isAvailable(player)) {
            Logger.getInstance().debug("Received login with an invalid player");
            return ModelResponse.INVALID_PARAMS;
        }

        freePlayers.add(new Player(player, age));
        notifyLobbyUpdate();
        return ModelResponse.ALLOW;
    }

    /**
     * Handle the player disconnection
     * This method must always return ModelResponse.ALLOW
     * @param player The player disconnected
     * @return ModelResponse.ALLOW
     */
    public ModelResponse logout(String player) {
        Optional<Player> optionalPlayer = getFreePlayer(player);

        if (optionalPlayer.isPresent()) {
            // The player was in the lobby

            freePlayers.remove(optionalPlayer.get());
            notifyLobbyUpdate();
            return ModelResponse.ALLOW;
        }

        Optional<Room> optionalRoom = getRoom(player);

        if (optionalRoom.isPresent()) {
            // The player is in a room, either the owner or just a player
            Room room = optionalRoom.get();

            if (room.getOwner().getName().equals(player)) {
                // The room owner has quit
                rooms.remove(room);

                if (room.getOtherPlayers().size() > 0) {
                    // Move the players in the deleted room into a new room
                    rooms.remove(room);

                    Player owner = room.getOtherPlayers().get(0);
                    Room replaced = new Room(owner, room.getMaxPlayers(), room.isSimpleGame());
                    room.getOtherPlayers().subList(1, room.getOtherPlayers().size()).forEach(replaced::addPlayer);

                    rooms.add(replaced);
                    notifyRoomUpdate(replaced);
                }

            } else {
                // The player was in another player's room
                optionalPlayer = getRoomOtherPlayer(room, player);

                if (optionalPlayer.isEmpty()) {
                    return ModelResponse.ALLOW;
                }

                room.removePlayer(optionalPlayer.get());
            }

            notifyLobbyUpdate();
            return ModelResponse.ALLOW;
        }

        Optional<Game> optionalGame = getGame(player);

        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();

            optionalPlayer = getGamePlayer(game, player);

            if (optionalPlayer.isEmpty()) {
                return ModelResponse.ALLOW;
            }

            game.logout(player);
        }

        return ModelResponse.ALLOW;
    }

    /**
     * Moves a free player into a new room, using that player as the owner
     * @param player The player
     * @param maxPlayers The players needed to start the game
     * @param simpleGame True if the game should have gods
     * @return the response - ModelResponse.ALLOW if the player can create a room
     */
    public ModelResponse createRoom(String player, int maxPlayers, boolean simpleGame) {
        Optional<Player> foundPlayer = getFreePlayer(player);

        if (foundPlayer.isEmpty()) {
            // Trying to create a room while already in a room or in game
            return ModelResponse.INVALID_STATE;
        }

        if (maxPlayers <= 1 || maxPlayers > MAX_ROOM_PLAYERS) {
            // Trying to create a room with an invalid player limit
            return ModelResponse.INVALID_STATE;
        }

        Room room = new Room(foundPlayer.get(), maxPlayers, simpleGame);
        rooms.add(room);
        freePlayers.remove(foundPlayer.get());
        notifyLobbyUpdate();
        notifyRoomUpdate(room);
        return ModelResponse.ALLOW;
    }

    /**
     * Moves a free player into an existing room
     * @param player The player
     * @param owner The owner that identifies the room
     * @return the response - ModelResponse.ALLOW if the player can join the room
     */
    public ModelResponse joinRoom(String player, String owner) {
        Optional<Player> foundPlayer = getFreePlayer(player);

        if (foundPlayer.isEmpty()) {
            // Trying to join a room while already in a room or in game
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

        // Always notify a room update so that the client can use this as a response
        notifyRoomUpdate(room);

        if (room.isFull()) {
            startGame(room);
            return ModelResponse.ALLOW;
        }

        return ModelResponse.ALLOW;
    }

    /**
     * Obtain the current game that the player is playing
     * @param player The player
     * @return Optional.empty() if there player is not in a game, the game otherwise
     */
    public Optional<Game> getGame(String player) {
        return games.stream().filter(game -> game.getAllPlayers().stream().anyMatch(other -> other.getName().equals(player))).findFirst();
    }

    private void startGame(Room room) {
        rooms.remove(room);
        notifyGameStart(room);

        Game game = new Game(provider);
        game.init(room.getAllPlayers(), deck, room.isSimpleGame());
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

        List<String> players = freePlayers.stream().map(Player::getName).collect(Collectors.toList());

        for (Player player : freePlayers) {
            provider.getLobbyUpdateEventObservable().notifyObservers(
                    new LobbyUpdateEvent(player.getName(), players, roomInfos)
            );
        }
    }

    private Optional<Player> getFreePlayer(String name) {
        return freePlayers.stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    private Optional<Player> getRoomOtherPlayer(Room room, String name) {
        return room.getOtherPlayers().stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    private Optional<Player> getGamePlayer(Game game, String name) {
        return game.getAllPlayers().stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    private Optional<Room> getRoom(String name) {
        return rooms.stream().filter(room -> room.getAllPlayers().stream().anyMatch(player -> player.getName().equals(name))).findFirst();
    }

    private Optional<Room> getRoomByOwner(String name) {
        return rooms.stream().filter(room -> room.getOwner().getName().equals(name)).findFirst();
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

    private boolean isAvailable(String player) {
        return !isFree(player) && !isInRoom(player) && !isInGame(player);
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
