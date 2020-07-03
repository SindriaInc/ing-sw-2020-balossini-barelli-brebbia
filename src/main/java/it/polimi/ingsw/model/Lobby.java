package it.polimi.ingsw.model;

import it.polimi.ingsw.common.IPlayerChecker;
import it.polimi.ingsw.common.info.RoomInfo;
import it.polimi.ingsw.common.event.lobby.LobbyGameStartEvent;
import it.polimi.ingsw.common.event.lobby.LobbyRoomUpdateEvent;
import it.polimi.ingsw.common.event.lobby.LobbyUpdateEvent;
import it.polimi.ingsw.common.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The class representing the lobby of the game. A player enters the lobby after connecting to the server and here
 * he can choose a room to join or create a new one. The lobby contains the list of rooms associated to the players.
 */
public class Lobby {

    private static final int MIN_ROOM_PLAYERS = 2;
    private static final int MAX_ROOM_PLAYERS = 4;

    private final ModelEventProvider provider;

    private final List<Player> freePlayers = new ArrayList<>();

    private final List<Room> rooms = new ArrayList<>();

    private final List<Game> games = new ArrayList<>();

    private final Deck deck;

    /**
     * Class constructor
     * @param deck The chosen deck of god
     */
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
                optionalPlayer.ifPresent(room::removePlayer);
            }

            notifyLobbyUpdate();
            return ModelResponse.ALLOW;
        }

        Optional<Game> optionalGame = getGame(player);

        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();

            optionalPlayer = getGamePlayer(game, player);
            optionalPlayer.ifPresent(modelPlayer -> game.logout(modelPlayer.getName()));
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

        if (maxPlayers < MIN_ROOM_PLAYERS || maxPlayers > MAX_ROOM_PLAYERS) {
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

        room.addPlayer(foundPlayer.get());
        freePlayers.remove(foundPlayer.get());

        // Always notify a room update so that the client can use this as a response
        notifyRoomUpdate(room);

        if (room.isFull()) {
            startGame(room);
            notifyLobbyUpdate();
            return ModelResponse.ALLOW;
        }

        notifyLobbyUpdate();
        return ModelResponse.ALLOW;
    }

    /**
     * Obtains the current game that the player is playing
     * @param player The player
     * @return Optional.empty() if there player is not in a game, the game otherwise
     */
    public Optional<Game> getGame(String player) {
        return games.stream().filter(game -> game.getAllPlayers().stream().anyMatch(other -> other.getName().equals(player))).findFirst();
    }

    /**
     * Start the game in the selected room
     * @param room The selected room
     */
    private void startGame(Room room) {
        rooms.remove(room);
        notifyGameStart(room);

        Game game = new Game(provider);
        game.init(room.getAllPlayers(), deck, room.isSimpleGame());
        games.add(game);
    }

    /**
     * Notifies the game start to the player in a selected room
     * @param room The selected room
     */
    private void notifyGameStart(Room room) {
        RoomInfo roomInfo = generateRoomInfo(room);

        for (Player other : room.getAllPlayers()) {
            new LobbyGameStartEvent(other.getName(), roomInfo).accept(provider);
        }
    }

    /**
     * Notifies the room update (join or leave) in a selected room
     * @param room The selected room
     */
    private void notifyRoomUpdate(Room room) {
        RoomInfo roomInfo = generateRoomInfo(room);

        for (Player other : room.getAllPlayers()) {
            new LobbyRoomUpdateEvent(other.getName(), roomInfo).accept(provider);
        }
    }
    /**
     * Notifies them lobby update (join, leave, enter or exit a room)
     */
    private void notifyLobbyUpdate() {
        List<RoomInfo> roomInfos = generateRoomInfos();

        List<String> players = freePlayers.stream().map(Player::getName).collect(Collectors.toList());

        for (Player player : freePlayers) {
            new LobbyUpdateEvent(player.getName(), players, roomInfos, MIN_ROOM_PLAYERS, MAX_ROOM_PLAYERS).accept(provider);
        }
    }

    /**
     * Obtains a free player in the lobby
     * @param name The name of the player
     */
    private Optional<Player> getFreePlayer(String name) {
        return freePlayers.stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    /**
     * Obtains the other players in the room of a selected player
     * @param room the selected room
     * @param name The name of the player
     */
    private Optional<Player> getRoomOtherPlayer(Room room, String name) {
        return room.getOtherPlayers().stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    /**
     * Obtains the other players in the game of a selected player
     * @param game The selected room
     * @param name The name of the player
     */
    private Optional<Player> getGamePlayer(Game game, String name) {
        return game.getAllPlayers().stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    /**
     * Obtains the room where the selected player is present (if exist)
     * @param name The name of the player
     */
    private Optional<Room> getRoom(String name) {
        return rooms.stream().filter(room -> room.getAllPlayers().stream().anyMatch(player -> player.getName().equals(name))).findFirst();
    }

    /**
     * Obtains the room where the selected player is the owner (if exist)
     * @param name The name of the player
     */
    private Optional<Room> getRoomByOwner(String name) {
        return rooms.stream().filter(room -> room.getOwner().getName().equals(name)).findFirst();
    }

    /**
     * Generates the infos of all the rooms
     * @return A list of room infos
     */
    private List<RoomInfo> generateRoomInfos() {
        List<RoomInfo> roomInfos = new ArrayList<>();

        for (Room room : rooms) {
            roomInfos.add(generateRoomInfo(room));
        }

        return roomInfos;
    }

    /**
     * Generates the info of a selected room
     * @param room The selected room
     */
    private RoomInfo generateRoomInfo(Room room) {
        return new RoomInfo(
                room.getOwner().getName(),
                room.getOtherPlayers().stream().map(Player::getName).collect(Collectors.toList()),
                room.getMaxPlayers(),
                room.isSimpleGame());
    }

    /**
     * Checks if a player is available
     * @param player The selected player
     * @return True if the player is available
     */
    private boolean isAvailable(String player) {
        return !isFree(player) && !isInRoom(player) && !isInGame(player);
    }

    /**
     * Checks if a player is free
     * @param name The player's name
     * @return True if the player is free
     */
    private boolean isFree(String name) {
        return getFreePlayer(name).isPresent();
    }

    /**
     * Checks if a player is in a room
     * @param name The player's name
     * @return True if the player is in a room
     */
    private boolean isInRoom(String name) {
        return rooms.stream().anyMatch(
                room -> room.getOwner().getName().equals(name) ||
                room.getOtherPlayers().stream().anyMatch(other -> other.getName().equals(name))
        );
    }

    /**
     * Checks if a player is in a game
     * @param name The player's name
     * @return True if the player is in a game
     */
    private boolean isInGame(String name) {
        return games.stream().anyMatch(
                game -> game.getAllPlayers().stream().anyMatch(other -> other.getName().equals(name))
        );
    }

}
